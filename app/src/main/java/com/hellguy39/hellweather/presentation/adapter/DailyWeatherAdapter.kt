package com.hellguy39.hellweather.presentation.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.DailyWeatherItemBinding
import com.hellguy39.hellweather.domain.model.DailyWeather
import com.hellguy39.hellweather.presentation.activities.main.IconHelper
import com.hellguy39.hellweather.utils.formatAsDay
import com.hellguy39.hellweather.utils.toPercents
import kotlin.math.roundToInt

class DailyWeatherAdapter(
    private val dataSet: List<DailyWeather>,
    private val callback: DailyWeatherItemCallback,
    private val resources: Resources
): RecyclerView.Adapter<DailyWeatherAdapter.DailyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        return DailyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.daily_weather_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        holder.bind(dataSet[position], position, callback)
    }

    override fun getItemCount(): Int = dataSet.size

    inner class DailyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = DailyWeatherItemBinding.bind(itemView)

        fun bind(
            dailyWeather: DailyWeather,
            position: Int,
            callback: DailyWeatherItemCallback
        ) {
            binding.tvDate.text = if (position == 0) "Today" else dailyWeather.date?.formatAsDay()

            Glide.with(itemView)
                .load(IconHelper.getByIconId(dailyWeather.weather?.get(0)))
                .into(binding.ivIcon)

            binding.chipMinMaxTemp.text = resources.getString(
                R.string.chip_min_max_temp,
                dailyWeather.temp?.max?.roundToInt(),
                dailyWeather.temp?.min?.roundToInt()
            )
            binding.chipPop.text = resources.getString(R.string.value_in_percents, dailyWeather.pop.toPercents())

            binding.rootCard.transitionName = R.string.daily_details_transition.toString() + position.toString()
            binding.rootCard.setOnClickListener {
                callback.onClick(dailyWeather, position, binding.rootCard)
            }
        }
    }

    interface DailyWeatherItemCallback {
        fun onClick(dailyWeather: DailyWeather, position: Int, itemView: View)
    }
}