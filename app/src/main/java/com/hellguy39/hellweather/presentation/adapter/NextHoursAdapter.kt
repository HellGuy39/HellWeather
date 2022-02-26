package com.hellguy39.hellweather.presentation.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.HourItemBinding
import com.hellguy39.hellweather.domain.models.weather.HourlyWeather
import com.hellguy39.hellweather.utils.STANDARD
import java.text.SimpleDateFormat
import java.util.*

class NextHoursAdapter(
    private val context: Context,
    private val hourList: MutableList<HourlyWeather>,
    private val units: String,
    private val resources: Resources
    ) : RecyclerView.Adapter<NextHoursAdapter.HourViewHolder> () {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NextHoursAdapter.HourViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.hour_item, parent, false)
        return HourViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: NextHoursAdapter.HourViewHolder,
        position: Int)
    {
        val hourlyWeather: HourlyWeather = hourList[position]
        holder.bind(hourlyWeather, context, position, units, resources)
    }

    override fun getItemCount(): Int = hourList.size

    class HourViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private val _binding = HourItemBinding.bind(v)

        fun bind(
            hourlyWeather: HourlyWeather,
            context: Context,
            position: Int, units: String,
            resources: Resources
        ) {
            if (units == STANDARD)
                _binding.tvTemp.text = String.format(resources.getString(R.string.item_temp_kelvin), hourlyWeather.temp)
            else
                _binding.tvTemp.text = String.format(resources.getString(R.string.item_temp_degree), hourlyWeather.temp)

            if (position == 0)
            {
                _binding.tvHour.text = resources.getString(R.string.now)
            }
            else
            {
                _binding.tvHour.text = SimpleDateFormat(
                    "HH:mm",
                    Locale.getDefault()
                ).format(Date(hourlyWeather.dt * 1000))//hourlyWeather.dt
            }

            Glide.with(context)
                .load("https://openweathermap.org/img/wn/${hourlyWeather.icon}@2x.png")
                //.dontAnimate()
                .placeholder(R.drawable.ic_round_image_not_supported_24)
                .error(R.drawable.ic_outline_error_outline_24)
                .centerCrop()
                .into(_binding.ivIcon)
        }

        override fun onClick(p0: View?) {

        }
    }

}