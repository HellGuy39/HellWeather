package com.hellguy39.hellweather.presentation.fragments.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.hellguy39.hellweather.databinding.FragmentHomeBinding
import com.hellguy39.hellweather.utils.OPEN_WEATHER_API_KEY
import com.hellguy39.hellweather.repository.database.pojo.CurrentWeather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
//import okhttp3.*
import com.google.gson.JsonObject
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.repository.database.pojo.DailyWeather
import com.hellguy39.hellweather.repository.database.pojo.HourlyWeather
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.repository.server.Common
import com.hellguy39.hellweather.repository.server.ApiService
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, HomeViewModelFactory(requireContext())).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        binding.rootView.setBackgroundResource(R.drawable.gradient_clear_day)

        binding.btnMenu.setOnClickListener {
            (activity as MainActivity).openDrawer()
        }

        viewModel.currentWeatherLive.observe(this, {
            updateUI(it)
        })
    }



    override fun onStart() {
        super.onStart()

        if (viewModel.isUserLocationLive.value == true)
        {
            viewModel.requestToApi()
        }
        else
        {

        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateUI(wm : CurrentWeather) {
        CoroutineScope(Main).launch {
            //binding.rootView.isRefreshing = false

            //Center
            Glide.with(this@HomeFragment)
                .load("https://openweathermap.org/img/wn/${wm.icon}@2x.png")
                .centerCrop()
                .into(binding.ivWeather)

//            when (wm.wMain) {
//                "Clouds" -> {}
//                "Clear" -> {}
//                //"Atmosphere" -> {}
//                "Snow" -> {}
//                "Rain" -> {}
//                "Drizzle" -> {}
//                "Thunderstorm" -> {}
//            }

            binding.tvTemp.text = wm.temp + "°"
            //binding.tvMaxTemp.text = wm.tempMax
            //binding.tvMinTemp.text = wm.tempMin
            binding.tvWeather.text = wm.wDescription
            //Top
            binding.tvUpdateTime.text = wm.dt
            binding.tvCity.text = viewModel.usrLoc.cityName
            //Details
            binding.tvSunrise.text = wm.sunrise
            binding.tvSunset.text = wm.sunset
            binding.tvTempFeelsLike.text = wm.tempFeelsLike + "°"
            binding.tvHumidity.text = wm.humidity + "%"
            binding.tvPressure.text = wm.pressure + "hPa"
            binding.tvWind.text = wm.windSpeed + "m/s"

        }
    }
}