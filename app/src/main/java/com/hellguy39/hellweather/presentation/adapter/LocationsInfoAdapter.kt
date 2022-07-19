package com.hellguy39.hellweather.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.LocationItemBinding
import com.hellguy39.hellweather.domain.model.LocationInfo
import com.hellguy39.hellweather.utils.setImageAsync

class LocationsInfoAdapter(
    private val dataSet: List<LocationInfo>,
    private val type: SearchType,
    private val callback: LocationInfoCallback
) : RecyclerView.Adapter<LocationsInfoAdapter.LocationInfoHolder>() {

    interface LocationInfoCallback {
        fun onClick(locationInfo: LocationInfo)
    }

    enum class SearchType {
        Recent,
        Search
    }

    inner class LocationInfoHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val binding = LocationItemBinding.bind(itemView)

        fun bind(locationInfo: LocationInfo, type: SearchType, callback: LocationInfoCallback) {
            binding.run {

                tvLocationFullName.text = locationInfo.country + ", " + locationInfo.state + ", " + locationInfo.name
                tvPosition.text = "Lat: ${locationInfo.lat}, lon: ${locationInfo.lon}"

                if (type == SearchType.Recent) {
                    ivIcon.setImageAsync(R.drawable.ic_schedule)
                } else {
                    ivIcon.setImageAsync(R.drawable.ic_search)
                }

                root.setOnClickListener {
                    callback.onClick(locationInfo = locationInfo)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationInfoHolder {
        return LocationInfoHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.location_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: LocationInfoHolder, position: Int) {
        holder.bind(
            locationInfo = dataSet[position],
            type = type,
            callback = callback
        )
    }

    override fun getItemCount(): Int = dataSet.size
}