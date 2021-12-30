package com.hellguy39.hellweather

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.hellguy39.hellweather.databinding.FragmentAddCityBinding
import com.hellguy39.hellweather.retrofit.Common
import com.hellguy39.hellweather.retrofit.RetrofitServices
import com.hellguy39.hellweather.utils.METRIC
import com.hellguy39.hellweather.utils.OPEN_WEATHER_API_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddCityFragment : Fragment() {

    private lateinit var binding: FragmentAddCityBinding
    private lateinit var mService: RetrofitServices

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

        binding = FragmentAddCityBinding.bind(view)

        binding.btnCnt.setOnClickListener {

            it.isEnabled = false
            val input = binding.etCity.text.toString()

            CoroutineScope(Dispatchers.Default).launch {
                if (checkTextField(input))
                {
                    binding.progressLinear.visibility = View.VISIBLE
                    binding.tvWait.visibility = View.VISIBLE

                    if (checkCityInAPI(input))
                    {
                        view.findNavController().navigate(R.id.action_addCityFragment_to_homeFragment)
                    }
                    else
                    {
                        binding.progressLinear.visibility = View.INVISIBLE
                        binding.tvWait.visibility = View.INVISIBLE

                        Snackbar.make(view,"City not found", Snackbar.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    Snackbar.make(view,"Not funny", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkTextField(input: String) : Boolean {
        return !TextUtils.isEmpty(input) && input.length > 3
    }

    private fun saveCord(lat: Double, lon: Double, cityName: String, region: String, cod: Int, timezone: Int, id: Int) {

    }

    private fun checkCityInAPI(input: String) : Boolean {
        mService.getCurrentWeather(input, METRIC, OPEN_WEATHER_API_KEY)
            .enqueue(object : Callback<JsonObject> {

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    if (response.isSuccessful)
                    {
                        val jObj: JsonObject? = response.body()
                        if (jObj != null)
                        {
                            val coordinates = jObj.getAsJsonObject("coord")
                            val lat: Double = coordinates.get("lat").asDouble
                            val lon: Double = coordinates.get("lon").asDouble
                            //saveCord(lat, lon)

                        }
                        else
                        {

                        }
                    }
                    else
                    {
                        //response.code().toString()
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }

            })
        return true
    }
}