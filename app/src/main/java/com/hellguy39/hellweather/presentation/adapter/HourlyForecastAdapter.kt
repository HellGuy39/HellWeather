package com.hellguy39.hellweather.presentation.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.HourlyWeatherItemBinding
import com.hellguy39.hellweather.domain.model.HourlyWeather
import com.hellguy39.hellweather.helpers.IconHelper
import com.hellguy39.hellweather.utils.formatAsHour
import kotlin.math.roundToInt

class HourlyForecastAdapter(
    private val dataSet: List<HourlyWeather>,
    private val callback: HourlyWeatherItemCallback,
    private val resources: Resources
): RecyclerView.Adapter<HourlyForecastAdapter.HourlyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        return  HourlyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.hourly_weather_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        holder.bind(dataSet[position], position, callback)
    }

    override fun getItemCount(): Int = dataSet.size

    inner class HourlyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = HourlyWeatherItemBinding.bind(itemView)

        fun bind(
            hourlyWeather: HourlyWeather,
            position: Int,
            callback: HourlyWeatherItemCallback
        ) {
            binding.tvDate.text = if (position == 0) " Now " else hourlyWeather.date?.formatAsHour()

            binding.tvTemp.text = resources.getString(R.string.value_as_temp, hourlyWeather.temp?.roundToInt())

            Glide.with(itemView)
                .load(IconHelper.getByIconId(hourlyWeather.weather?.get(0)))
                .into(binding.ivIcon)

            binding.rootCard.transitionName = R.string.hourly_details_transition.toString() + position.toString()
            binding.rootCard.setOnClickListener {
                callback.onClick(hourlyWeather, position, binding.rootCard)
            }
        }
    }

    interface HourlyWeatherItemCallback {
        fun onClick(hourlyWeather: HourlyWeather, position: Int, itemView: View)
    }
}