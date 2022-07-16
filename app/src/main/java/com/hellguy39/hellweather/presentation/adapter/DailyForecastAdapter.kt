package com.hellguy39.hellweather.presentation.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.DailyWeatherItemBinding
import com.hellguy39.hellweather.domain.model.DailyWeather
import com.hellguy39.hellweather.format.DateFormatter
import com.hellguy39.hellweather.helpers.IconHelper
import com.hellguy39.hellweather.utils.setImageAsync
import com.hellguy39.hellweather.utils.toPercents
import kotlin.math.roundToInt

class DailyForecastAdapter(
    private val dataSet: List<DailyWeather>,
    private val callback: DailyWeatherItemCallback,
    private val resources: Resources
): RecyclerView.Adapter<DailyForecastAdapter.DailyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        return DailyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.daily_weather_item, parent, false)
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
            binding.run {
                tvDate.text =
                    if (position == 0)
                        resources.getString(R.string.text_today)
                    else
                        DateFormatter.format(dailyWeather.date, DateFormatter.WEEK_DAY)

                ivIcon.setImageAsync(IconHelper.getByIconId(dailyWeather.weather?.get(0)))
                chipMinMaxTemp.text = resources.getString(
                    R.string.chip_min_max_temp,
                    dailyWeather.temp?.max?.roundToInt(),
                    dailyWeather.temp?.min?.roundToInt()
                )
                chipPop.text = resources.getString(R.string.text_value_percents, dailyWeather.pop.toPercents())

                rootCard.transitionName = R.string.daily_details_transition.toString() + position.toString()
                rootCard.setOnClickListener {
                    callback.onClick(dailyWeather, position, binding.rootCard)
                }
            }
        }
    }

    interface DailyWeatherItemCallback {
        fun onClick(dailyWeather: DailyWeather, position: Int, itemView: View)
    }
}