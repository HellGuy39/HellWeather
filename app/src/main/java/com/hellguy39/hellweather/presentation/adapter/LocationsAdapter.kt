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
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.repository.database.pojo.DailyWeather
import com.hellguy39.hellweather.repository.database.pojo.UserLocation

class LocationsAdapter(
    private val context: Context,
    private val locationList: List<UserLocation>,
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
        //val userLocation: UserLocation = locationList[position]
        holder.bind(locationList, context, position)
    }

    override fun getItemCount(): Int = locationList.size

    class LocationViewHolder(v: View, private val listener: EventListener) : RecyclerView.ViewHolder(v) {

        private var tvId: TextView
        private var tvLocationName: TextView
        private var tvTimezone: TextView
        private var tvLatLon: TextView
        private var btnEdit: Button
        private var btnDelete: Button
        private var card: MaterialCardView


        init {
            tvId = v.findViewById(R.id.tvId)
            tvLocationName = v.findViewById(R.id.tvLocationName)
            tvTimezone = v.findViewById(R.id.tvTimezone)
            tvLatLon = v.findViewById(R.id.tvLanLon)

            btnEdit = v.findViewById(R.id.btnEdit)
            btnDelete = v.findViewById(R.id.btnDelete)
            card = v.findViewById(R.id.rootCard)
        }

        fun bind(locationList: List<UserLocation>, context: Context, position: Int) {

            btnDelete.setOnClickListener {
                listener.onDelete(locationList[position])
            }

            btnEdit.setOnClickListener {
                listener.onEdit(locationList[position])
            }

            if (locationList[position].id == 1) {
                tvId.text = "Main"
            } else {
                tvId.text =locationList[position].id.toString()
            }
            tvLocationName.text = locationList[position].locationName
            tvLatLon.text = "Lat: ${locationList[position].lat} | Lon: ${locationList[position].lon}"

            if (locationList[position].timezone == 0)
            {
                tvTimezone.text = "${locationList[position].timezone} GMT"
            }
            else if (locationList[position].timezone > 0)
            {
                tvTimezone.text = "+${locationList[position].timezone} GMT"
            }
            else
            {
                tvTimezone.text = "${locationList[position].timezone} GMT"
            }

        }

    }

}