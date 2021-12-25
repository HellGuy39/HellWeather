package com.hellguy39.hellweather

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hellguy39.hellweather.databinding.FragmentHomeBinding
import com.hellguy39.hellweather.utils.OW_API_KEY
import com.hellguy39.hellweather.utils.WeatherModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    var okHttpClient: OkHttpClient = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
        binding.rootView.setOnRefreshListener {
            onRefresh()
        }

    }

    override fun onStart() {
        super.onStart()
        onRefresh()
    }

    private fun onRefresh() {
        CoroutineScope(IO).launch {
            requestAPI()
            updateUI()
        }
    }

    private suspend fun updateUI() {

    }

    private suspend fun requestAPI() {
        val city = "Krasnodar"
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$OW_API_KEY"
        val request: Request = Request
            .Builder()
            .url(url)
            .build()

        okHttpClient
            .newCall(request)
            .enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException)
            {

            }

            override fun onResponse(call: Call, response: Response)
            {
                //val wm = WeatherModel()

                if (response.isSuccessful)
                {
                    val result = response.body?.string()
                    result?.let { Log.i("LOG", it) }
                    val jsonObject = JSONObject(result)
                    val main = jsonObject.getJSONObject("main")
                    val temp = main.getString("temp")
                    Log.i("LOG_TAG", temp)
                }
                else
                {

                }
            }
        })
    }
}