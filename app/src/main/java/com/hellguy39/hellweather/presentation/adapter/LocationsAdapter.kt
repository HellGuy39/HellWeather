package com.hellguy39.hellweather.presentation.adapter

import android.content.Context
import android.media.metrics.Event
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.gson.JsonObject
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.LocationItemBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.repository.database.pojo.DailyWeather
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import com.hellguy39.hellweather.utils.Converter
import java.text.SimpleDateFormat
import java.util.*

class LocationsAdapter(
    private val context: Context,
    private val locationList: List<UserLocation>,
    private val weatherJsonList: List<JsonObject>,
    private val listener: EventListener
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
        if (weatherJsonList.isNotEmpty()) {
            holder.bindWithWeather(locationList, weatherJsonList, position)
        }
        else
        {
            holder.bind(locationList, position)
        }
    }

    override fun getItemCount(): Int = locationList.size

    class LocationViewHolder(v: View, private val listener: EventListener) : RecyclerView.ViewHolder(v) {

        private val _binding = LocationItemBinding.bind(v)

        fun bind(locationList: List<UserLocation>, position: Int) {
            _binding.tvLocationName.text = locationList[position].locationName
        }

        fun bindWithWeather(locationList: List<UserLocation>, weatherJsonList: List<JsonObject>, position: Int) {

            val converter = Converter()
            val weatherObject = converter.toWeatherObject(weatherJsonList[position])

            _binding.tvLocationName.text = locationList[position].locationName
            _binding.tvWeatherDescription.text = weatherObject.currentWeather.wDescription
            _binding.tvTemp.text = weatherObject.currentWeather.temp + "°"
            _binding.tvTempMinMax.text = "Max.: ${weatherObject.currentWeather.tempMax}°, min.: ${weatherObject.currentWeather.tempMin}°"
            _binding.tvTime.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(weatherObject.currentWeather.dt * 1000))
        }
    }

}