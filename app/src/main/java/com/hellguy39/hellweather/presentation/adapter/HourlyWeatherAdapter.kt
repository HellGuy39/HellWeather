package com.hellguy39.hellweather.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.HourlyWeatherItemBinding
import com.hellguy39.hellweather.domain.model.HourlyWeather
import com.hellguy39.hellweather.utils.formatAsHour

class HourlyWeatherAdapter(
    private val dataSet: List<HourlyWeather>
): RecyclerView.Adapter<HourlyWeatherAdapter.HourlyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        return  HourlyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.hourly_weather_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int = dataSet.size

    inner class HourlyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = HourlyWeatherItemBinding.bind(itemView)

        fun bind(hourlyWeather: HourlyWeather) {
            binding.tvDate.text = hourlyWeather.date?.formatAsHour()
        }
    }
}