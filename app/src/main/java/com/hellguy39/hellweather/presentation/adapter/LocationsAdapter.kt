package com.hellguy39.hellweather.presentation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.repository.database.pojo.DailyWeather
import com.hellguy39.hellweather.repository.database.pojo.UserLocation

class LocationsAdapter(
    private val context: Context,
    private val locationList: List<UserLocation>
    ) : RecyclerView.Adapter<LocationsAdapter.LocationViewHolder> () {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationsAdapter.LocationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.location_item, parent, false)
        return LocationViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: LocationsAdapter.LocationViewHolder,
        position: Int)
    {
        val userLocation: UserLocation = locationList[position]
        holder.bind(userLocation, context)
    }

    override fun getItemCount(): Int = locationList.size

    class LocationViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

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

        fun bind(usrLoc: UserLocation, context: Context) {
            btnDelete.setOnClickListener(this)
            btnEdit.setOnClickListener(this)

            if (usrLoc.id == 1) {
                tvId.text = "Main"
            } else {
                tvId.text = usrLoc.id.toString()
            }
            tvLocationName.text = usrLoc.locationName
            tvLatLon.text = "Lat: ${usrLoc.lat} | Lon: ${usrLoc.lon}"

            if (usrLoc.timezone > 0)
            {
                tvTimezone.text = "+${usrLoc.timezone} GMT"
            }
            else
            {
                tvTimezone.text = "${usrLoc.timezone} GMT"
            }

        }

        override fun onClick(p0: View?) {
            when (p0?.id) {
                R.id.btnDelete -> {

                }
                R.id.btnEdit -> {

                }
            }
        }
    }

}