package com.hellguy39.hellweather.presentation.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.CurrentWeatherDetailItemBinding
import com.hellguy39.hellweather.domain.model.CurrentWeather
import com.hellguy39.hellweather.domain.model.DailyWeather
import com.hellguy39.hellweather.domain.model.HourlyWeather
import com.hellguy39.hellweather.helpers.DetailHelper
import com.hellguy39.hellweather.utils.formatAsHour
import com.hellguy39.hellweather.utils.setImageAsync
import com.hellguy39.hellweather.utils.toKilometers
import kotlin.math.roundToInt

class DetailsAdapter(
   private val dataSet: List<DetailModel>,
   private val resources: Resources
): RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailsViewHolder {
        return DetailsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.current_weather_detail_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int = dataSet.size

    inner class DetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val binding = CurrentWeatherDetailItemBinding.bind(itemView)

        fun bind(detailModel: DetailModel) {
            binding.run {
                ivIcon.setImageAsync(DetailHelper.getIcon(detailModel.type))
                tvTitle.text = resources.getString(DetailHelper.getTitle(detailModel.type))
                tvAnyValue.text = detailModel.value
            }
        }
    }
    companion object {
        const val SPAN_COUNT = 3
    }
}

internal fun CurrentWeather.toDetailsModelList(resources: Resources): List<DetailModel> {
    return listOf(
        DetailModel(DetailType.Wind, resources.getString(R.string.text_value_meters_per_sec, this.windSpeed?.roundToInt())),
        DetailModel(DetailType.Humidity, resources.getString(R.string.text_value_percents, this.humidity)),
        DetailModel(DetailType.Pressure, resources.getString(R.string.text_value_pressure, this.pressure)),
        DetailModel(DetailType.UVI, this.uvi?.roundToInt().toString()),
        DetailModel(DetailType.Visibility, this.visibility.toKilometers()),
        DetailModel(DetailType.DewPoint, resources.getString(R.string.text_value_temp, this.dewPoint?.roundToInt())),
//        DetailModel(DetailType.Sunrise, this.sunrise?.formatAsHour()),
//        DetailModel(DetailType.Sunset, this.sunset?.formatAsHour()),
//        DetailModel(DetailType.Clouds, resources.getString(R.string.value_in_percents, this.clouds)),
    )
}

internal fun HourlyWeather.toDetailsModelList(resources: Resources): List<DetailModel> {
    return listOf(
        DetailModel(DetailType.Humidity, resources.getString(R.string.text_value_percents, this.humidity)),
        DetailModel(DetailType.Pressure, resources.getString(R.string.text_value_pressure, this.pressure)),
        DetailModel(DetailType.UVI, this.uvi?.roundToInt().toString()),
        DetailModel(DetailType.Visibility, this.visibility.toKilometers())
    )
}

internal fun DailyWeather.toDetailsModelList(resources: Resources): List<DetailModel> {
    return listOf(
        DetailModel(DetailType.Sunrise, this.sunrise?.formatAsHour()),
        DetailModel(DetailType.Humidity, resources.getString(R.string.text_value_percents, this.humidity)),
        DetailModel(DetailType.Pressure, resources.getString(R.string.text_value_pressure, this.pressure)),
        DetailModel(DetailType.Sunset, this.sunset?.formatAsHour()),
        DetailModel(DetailType.UVI, this.uvi?.roundToInt().toString()),
    )
}


data class DetailModel(
    val type: Enum<DetailType>,
    val value: String?,
)

enum class DetailType {
    Humidity,
    Pressure,
    Sunrise,
    Sunset,
    UVI,
    Visibility,
    Wind,
    DewPoint,
    Clouds
}