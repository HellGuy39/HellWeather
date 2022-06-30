package com.hellguy39.hellweather.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.DailyWeatherItemBinding
import com.hellguy39.hellweather.domain.model.DailyWeather
import com.hellguy39.hellweather.utils.formatAsDay

class DailyWeatherAdapter(
    private val dataSet: List<DailyWeather>
): RecyclerView.Adapter<DailyWeatherAdapter.DailyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        return DailyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.daily_weather_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int = dataSet.size

    inner class DailyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = DailyWeatherItemBinding.bind(itemView)

        fun bind(dailyWeather: DailyWeather) {
            binding.tvDate.text = dailyWeather.date?.formatAsDay()
        }
    }
}