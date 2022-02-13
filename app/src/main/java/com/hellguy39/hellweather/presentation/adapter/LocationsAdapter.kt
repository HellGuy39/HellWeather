package com.hellguy39.hellweather.presentation.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.LocationItemBinding
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import com.hellguy39.hellweather.repository.database.pojo.WeatherData
import com.hellguy39.hellweather.utils.IMPERIAL
import com.hellguy39.hellweather.utils.METRIC
import com.hellguy39.hellweather.utils.STANDARD
import java.text.SimpleDateFormat
import java.util.*

class LocationsAdapter(
    private val context: Context,
    private val locationList: List<UserLocation>,
    private val weatherDataList: List<WeatherData>,
    private val resources: Resources,
    private val listener: EventListener,
    private val units: String
    ) : RecyclerView.Adapter<LocationsAdapter.LocationViewHolder> () {

    interface EventListener {
        fun onDelete(userLocation: UserLocation)
        fun onEdit(userLocation: UserLocation)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.location_item, parent, false)
        return LocationViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(
        holder: LocationViewHolder,
        position: Int)
    {
        if (weatherDataList.isNotEmpty())
            holder.bindWithWeather(locationList, weatherDataList, position, units, context, resources)
    }

    override fun getItemCount(): Int = locationList.size

    class LocationViewHolder(v: View, private val listener: EventListener) : RecyclerView.ViewHolder(v) {

        private val _binding = LocationItemBinding.bind(v)

        fun bindWithWeather(
            locationList: List<UserLocation>,
            weatherDataList: List<WeatherData>,
            position: Int,
            units: String,
            context: Context,
            resources: Resources
        ) {

            val weatherItem = weatherDataList[position]

            _binding.tvLocationName.text = locationList[position].locationName
            _binding.tvWeatherDescription.text = weatherItem.currentWeather.wDescription

            if (units == STANDARD)
            {
                _binding.tvTemp.text = String.format(resources.getString(R.string.temp_kelvin), weatherItem.currentWeather.temp)
                _binding.tvTempMinMax.text = String.format(resources.getString(R.string.max_min_kelvin_text), weatherItem.currentWeather.tempMax, weatherItem.currentWeather.tempMin)
            }
            else if (units == METRIC)
            {
                _binding.tvTemp.text = String.format(resources.getString(R.string.temp_celsius), weatherItem.currentWeather.temp)
                _binding.tvTempMinMax.text = String.format(resources.getString(R.string.max_min_degree_text), weatherItem.currentWeather.tempMax, weatherItem.currentWeather.tempMin)
            }
            else if (units == IMPERIAL)
            {
                _binding.tvTemp.text = String.format(resources.getString(R.string.temp_fahrenheit), weatherItem.currentWeather.temp)
                _binding.tvTempMinMax.text = String.format(resources.getString(R.string.max_min_degree_text), weatherItem.currentWeather.tempMax, weatherItem.currentWeather.tempMin)
            }

            _binding.tvTime.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(weatherItem.currentWeather.dt * 1000))

            _binding.rootCard.setOnLongClickListener {
                MaterialAlertDialogBuilder(context)
                    //.setMessage("Delete action")
                    .setTitle(resources.getString(R.string.delete_location_text))
                    .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                        //Nothing
                    }
                    .setPositiveButton(resources.getString(R.string.yes)) { dialog, which ->
                        listener.onDelete(locationList[position])
                    }
                    .show()
                true
            }
        }
    }

}