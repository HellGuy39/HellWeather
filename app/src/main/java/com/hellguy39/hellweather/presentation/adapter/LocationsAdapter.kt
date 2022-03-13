package com.hellguy39.hellweather.presentation.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.LocationItemBinding
import com.hellguy39.hellweather.domain.models.param.UserLocationParam
import com.hellguy39.hellweather.domain.models.weather.WeatherData
import com.hellguy39.hellweather.domain.utils.Unit
import java.text.SimpleDateFormat
import java.util.*

class LocationsAdapter(
    private val locationList: List<UserLocationParam>?,
    private val weatherDataList: List<WeatherData>?,
    private val resources: Resources,
    private val units: String
    ) : RecyclerView.Adapter<LocationsAdapter.LocationViewHolder> () {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.location_item, parent, false)
        return LocationViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: LocationViewHolder,
        position: Int)
    {
        holder.bind(
            locationList = locationList,
            weatherDataList = weatherDataList,
            position = position,
            units = units,
            resources = resources
        )
    }

    override fun getItemCount(): Int = locationList?.size ?: 0

    class LocationViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val _binding = LocationItemBinding.bind(v)

        fun bind(
            locationList: List<UserLocationParam>?,
            weatherDataList: List<WeatherData>?,
            position: Int,
            units: String,
            resources: Resources
        ) {
            if (locationList == null)
                return

            _binding.tvLocationName.text = locationList[position].locationName

            if (weatherDataList.isNullOrEmpty())
                return

            val weatherItem = weatherDataList[position]

            _binding.tvWeatherDescription.text = weatherItem.currentWeather.wDescription

            when (units) {
                Unit.Standard.name -> {
                    _binding.tvTemp.text = String.format(resources.getString(R.string.temp_kelvin), weatherItem.currentWeather.temp)
                    _binding.tvTempMinMax.text = String.format(resources.getString(R.string.max_min_kelvin_text), weatherItem.currentWeather.tempMax, weatherItem.currentWeather.tempMin)
                }
                Unit.Metric.name -> {
                    _binding.tvTemp.text = String.format(resources.getString(R.string.temp_celsius), weatherItem.currentWeather.temp)
                    _binding.tvTempMinMax.text = String.format(resources.getString(R.string.max_min_degree_text), weatherItem.currentWeather.tempMax, weatherItem.currentWeather.tempMin)
                }
                Unit.Imperial.name -> {
                    _binding.tvTemp.text = String.format(resources.getString(R.string.temp_fahrenheit), weatherItem.currentWeather.temp)
                    _binding.tvTempMinMax.text = String.format(resources.getString(R.string.max_min_degree_text), weatherItem.currentWeather.tempMax, weatherItem.currentWeather.tempMin)
                }
            }

            _binding.tvTime.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(weatherItem.currentWeather.dt * 1000))

        }
    }

}