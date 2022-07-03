package com.hellguy39.hellweather.presentation.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.CurrentWeatherDetailItemBinding
import com.hellguy39.hellweather.domain.model.CurrentWeather
import com.hellguy39.hellweather.helpers.IconHelper
import com.hellguy39.hellweather.utils.formatAsHour
import com.hellguy39.hellweather.utils.toKilometers
import kotlin.math.roundToInt

class CurrentWeatherDetailsAdapter(
   private val dataSet: List<DetailModel>,
): RecyclerView.Adapter<CurrentWeatherDetailsAdapter.CurrentWeatherDetailsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrentWeatherDetailsViewHolder {
        return CurrentWeatherDetailsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.current_weather_detail_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CurrentWeatherDetailsViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int = dataSet.size

    inner class CurrentWeatherDetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val binding = CurrentWeatherDetailItemBinding.bind(itemView)

        fun bind(detailModel: DetailModel) {

            Glide.with(itemView)
                .load(IconHelper.getDetailIcon(detailModel.detail))
                .into(binding.ivIcon)

            binding.tvTitle.text = detailModel.detail.name
            binding.tvAnyValue.text = detailModel.value
        }
    }
    companion object {
        const val SPAN_COUNT = 3
    }
}

internal fun CurrentWeather.toDetailsModelList(resources: Resources): List<DetailModel> {
    return listOf(
        DetailModel(
            DetailTitle.Sunrise,
            this.sunrise?.formatAsHour()
        ),
        DetailModel(
            DetailTitle.Humidity,
            resources.getString(R.string.value_in_percents, this.humidity)
        ),
        DetailModel(
            DetailTitle.Pressure,
            resources.getString(R.string.value_as_pressure, this.pressure)
        ),
        DetailModel(
            DetailTitle.Sunset,
            this.sunset?.formatAsHour()
        ),
        DetailModel(
            DetailTitle.UVI,
            this.uvi?.roundToInt().toString()
        ),
        DetailModel(
            DetailTitle.Visibility,
            this.visibility.toKilometers()
        )
    )
}

data class DetailModel(
    val detail: Enum<DetailTitle>,
    val value: String?
)

enum class DetailTitle {
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