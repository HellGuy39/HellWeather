package com.hellguy39.hellweather.presentation.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.HourlyWeatherItemBinding
import com.hellguy39.hellweather.domain.model.HourlyWeather
import com.hellguy39.hellweather.helpers.IconHelper
import com.hellguy39.hellweather.utils.formatAsHour
import com.hellguy39.hellweather.utils.setImageAsync
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
            binding.run {
                ivIcon.setImageAsync(IconHelper.getByIconId(hourlyWeather.weather?.get(0)))
                tvDate.text =
                    if (position == 0)
                        resources.getString(R.string.text_now)
                    else
                        hourlyWeather.date?.formatAsHour()

                tvTemp.text = resources.getString(R.string.text_value_temp, hourlyWeather.temp?.roundToInt())
                rootCard.transitionName = R.string.hourly_details_transition.toString() + position.toString()
                rootCard.setOnClickListener {
                    callback.onClick(hourlyWeather, position, binding.rootCard)
                }
            }
        }
    }

    interface HourlyWeatherItemCallback {
        fun onClick(hourlyWeather: HourlyWeather, position: Int, itemView: View)
    }
}