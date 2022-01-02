package com.hellguy39.hellweather.presentation.fragments.add

import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentAddCityBinding
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import com.hellguy39.hellweather.repository.server.Common
import com.hellguy39.hellweather.repository.server.ApiService
import com.hellguy39.hellweather.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddCityFragment : Fragment() {

    private lateinit var binding: FragmentAddCityBinding
    private lateinit var mService: ApiService
    private lateinit var fragView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mService = Common.retrofitServices
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_add_city, container, false)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        fragView = view
        binding = FragmentAddCityBinding.bind(view)

        binding.btnCnt.setOnClickListener {

            val input = binding.etCity.text.toString()

            CoroutineScope(Dispatchers.Default).launch {
                if (checkTextField(input))
                {
                    loadController(ENABLE)
                    checkCityInAPI(input)
                }
                else
                {
                    Snackbar.make(view,"Not funny", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun loadController(action: String) {
        withContext(Dispatchers.Main) {
            when (action) {
                ENABLE -> {
                    binding.etCity.isEnabled = false
                    binding.btnCnt.isEnabled = false
                    binding.progressLinear.visibility = View.VISIBLE
                    binding.tvWait.visibility = View.VISIBLE
                }
                DISABLE -> {
                    binding.etCity.isEnabled = true
                    binding.btnCnt.isEnabled = true
                    binding.progressLinear.visibility = View.INVISIBLE
                    binding.tvWait.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun checkTextField(input: String) : Boolean {
        return !TextUtils.isEmpty(input) && input.length > 3
    }

    private fun saveCord(usrLoc: UserLocation) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val edit: SharedPreferences.Editor = sharedPreferences.edit()
        edit.putString("lat", usrLoc.lat)
        edit.putString("lon", usrLoc.lon)
        edit.putString("cityName", usrLoc.cityName)
        edit.apply()
    }

    private fun disableFirstBoot() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val edit: SharedPreferences.Editor = sharedPreferences.edit()
        edit.putBoolean("first_boot", false)
        edit.apply()
    }

    private fun onRequestResult(res: String, eCode: String = "0") {
        when(res) {
            SUCCESSFUL -> {
                CoroutineScope(Dispatchers.Default).launch {
                    loadController(DISABLE)
                    disableFirstBoot()
                }
                fragView.findNavController().navigate(AddCityFragmentDirections.actionAddCityFragmentToHomeFragment())
            }
            EMPTY_BODY -> {
                CoroutineScope(Dispatchers.Default).launch {
                    loadController(DISABLE)
                }
                Snackbar.make(fragView,"City not found", Snackbar.LENGTH_SHORT).show()
            }
            ERROR -> {
                CoroutineScope(Dispatchers.Default).launch {
                    loadController(DISABLE)
                }
                Snackbar.make(fragView,"City not found", Snackbar.LENGTH_SHORT).show()
            }
            FAILURE -> {
                CoroutineScope(Dispatchers.Default).launch {
                    loadController(DISABLE)
                }
                Snackbar.make(fragView,"Server not responding", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkCityInAPI(input: String) {

        mService.getCurrentWeather(input, METRIC, OPEN_WEATHER_API_KEY)
            .enqueue(object : Callback<JsonObject> {

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    if (response.isSuccessful)
                    {
                        val jObj: JsonObject? = response.body()
                        if (jObj != null)
                        {
                            val usrLoc = UserLocation()

                            val coordinates = jObj.getAsJsonObject("coord")
                            usrLoc.lat = coordinates.get("lat").asString
                            usrLoc.lon = coordinates.get("lon").asString

                            usrLoc.cityName = input
                            usrLoc.region = jObj.get("name").asString
                            usrLoc.cod = jObj.get("cod").asString
                            usrLoc.id = jObj.get("id").asInt
                            usrLoc.timezone = jObj.get("timezone").asString
                            saveCord(usrLoc)

                            Log.d("LOG", "Lat:${usrLoc.lat} Lon:${usrLoc.lon}")

                            if (usrLoc.lat != "N/A" || !TextUtils.isEmpty(usrLoc.lat) &&
                                usrLoc.lon != "N/A" || !TextUtils.isEmpty(usrLoc.lon)) {
                                onRequestResult(SUCCESSFUL)
                            }
                            else
                            {
                                onRequestResult(EMPTY_BODY)
                            }
                        }
                        else
                        {
                            onRequestResult(EMPTY_BODY)
                        }
                    }
                    else
                    {
                        onRequestResult(ERROR, response.code().toString())
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    onRequestResult(FAILURE, t.toString())
                }

            })
    }
}