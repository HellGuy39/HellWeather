package com.hellguy39.hellweather.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.CurrentWeatherDetailItemBinding
import com.hellguy39.hellweather.presentation.activities.main.IconHelper
import com.hellguy39.hellweather.utils.Detail

class CurrentWeatherDetailsAdapter(
   private val dataSet: List<DetailModel>
): RecyclerView.Adapter<CurrentWeatherDetailsAdapter.CurrentWeatherDetailsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrentWeatherDetailsViewHolder {
        return CurrentWeatherDetailsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.current_weather_detail_item, parent, false)
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

    data class DetailModel(
        val detail: Enum<Detail>,
        val value: String?
    )

    companion object {
        const val SPAN_COUNT = 3
    }
}