package com.hellguy39.hellweather.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.LocationItemBinding
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import com.hellguy39.hellweather.repository.database.pojo.WeatherData
import java.text.SimpleDateFormat
import java.util.*

class LocationsAdapter(
    private val context: Context,
    private val locationList: List<UserLocation>,
    private val weatherDataList: List<WeatherData>,
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
        if (weatherDataList.isNotEmpty())
            holder.bindWithWeather(locationList, weatherDataList, position, context)
    }

    override fun getItemCount(): Int = locationList.size

    class LocationViewHolder(v: View, private val listener: EventListener) : RecyclerView.ViewHolder(v) {

        private val _binding = LocationItemBinding.bind(v)

        fun bindWithWeather(locationList: List<UserLocation>, weatherDataList: List<WeatherData>, position: Int, context: Context) {

            val weatherItem = weatherDataList[position]

            _binding.tvLocationName.text = locationList[position].locationName
            _binding.tvWeatherDescription.text = weatherItem.currentWeather.wDescription
            _binding.tvTemp.text = weatherItem.currentWeather.temp + "°"
            _binding.tvTempMinMax.text = "Max.: ${weatherItem.currentWeather.tempMax}°, min.: ${weatherItem.currentWeather.tempMin}°"
            _binding.tvTime.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(weatherItem.currentWeather.dt * 1000))

            _binding.rootCard.setOnLongClickListener {
                MaterialAlertDialogBuilder(context)
                    //.setMessage("Delete action")
                    .setTitle("Do you want to delete this location?")
                    .setNegativeButton("Cancel") { dialog, which ->
                        //Nothing
                    }
                    .setPositiveButton("Yes, do it") { dialog, which ->
                        listener.onDelete(locationList[position])
                    }
                    .show()
                true
            }
        }
    }

}