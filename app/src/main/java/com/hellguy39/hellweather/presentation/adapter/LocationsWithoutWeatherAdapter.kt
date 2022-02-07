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

class LocationsWithoutWeatherAdapter(
    private val context: Context,
    private val locationList: List<UserLocation>,
    ) : RecyclerView.Adapter<LocationsWithoutWeatherAdapter.LocationWithoutWeatherViewHolder> () {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationWithoutWeatherViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.location_item, parent, false)
        return LocationWithoutWeatherViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: LocationWithoutWeatherViewHolder,
        position: Int)
    {
        holder.bind(locationList, position, context)
    }

    override fun getItemCount(): Int = locationList.size

    class LocationWithoutWeatherViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val _binding = LocationItemBinding.bind(v)

        fun bind(locationList: List<UserLocation>, position: Int, context: Context) {
            _binding.tvLocationName.text = locationList[position].locationName

            _binding.rootCard.setOnLongClickListener {
                MaterialAlertDialogBuilder(context)
                    //.setMessage("Delete action")
                    .setTitle("Do you want to delete this location?")
                    .setNegativeButton("Cancel") { dialog, which ->
                        // Respond to negative button press
                    }
                    .setPositiveButton("Yes, do it") { dialog, which ->
                        // Respond to positive button press
                    }
                    .show()
                true
            }
        }
    }

}