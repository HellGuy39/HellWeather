package com.hellguy39.hellweather.presentation.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.DayItemBinding
import com.hellguy39.hellweather.domain.models.weather.DailyWeather
import com.hellguy39.hellweather.utils.STANDARD

class NextDaysAdapter(
    private val context: Context,
    private val dayList: MutableList<DailyWeather>,
    private val units: String,
    private val resources: Resources
    ) : RecyclerView.Adapter<NextDaysAdapter.DayViewHolder> () {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NextDaysAdapter.DayViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.day_item, parent, false)
        return DayViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: NextDaysAdapter.DayViewHolder,
        position: Int)
    {
        val dailyWeather: DailyWeather = dayList[position]
        holder.bind(dailyWeather, context, units, resources)
    }

    override fun getItemCount(): Int = dayList.size

    class DayViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private val _binding = DayItemBinding.bind(v)

        fun bind(dailyWeather: DailyWeather, context: Context, units: String, resources: Resources) {

            if (units == STANDARD) {
                _binding.tvMaxTemp.text =
                    String.format(resources.getString(R.string.item_temp_kelvin), dailyWeather.max)
                _binding.tvMinTemp.text =
                    String.format(resources.getString(R.string.item_temp_kelvin), dailyWeather.min)
            } else {
                _binding.tvMaxTemp.text =
                    String.format(resources.getString(R.string.item_temp_degree), dailyWeather.max)
                _binding.tvMinTemp.text =
                    String.format(resources.getString(R.string.item_temp_degree), dailyWeather.min)
            }

            _binding.tvHumidity.text = String.format(resources.getString(R.string.pop_text), dailyWeather.pop.toInt())
            _binding.tvDay.text = dailyWeather.dt

            Glide.with(context)
                .load("https://openweathermap.org/img/wn/${dailyWeather.icon}@2x.png")
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