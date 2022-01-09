package com.hellguy39.hellweather.presentation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.repository.database.pojo.DailyWeather
import com.hellguy39.hellweather.repository.database.pojo.HourlyWeather
import java.text.SimpleDateFormat
import java.util.*

class NextHoursAdapter(
    private val context: Context,
    private val hourList: MutableList<HourlyWeather>
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
        holder.bind(hourlyWeather, context, position)
    }

    override fun getItemCount(): Int = hourList.size

    class HourViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var tvTemp: TextView
        private var tvHour: TextView
        private var ivIcon : ImageView

        init {
            tvTemp = v.findViewById(R.id.tvTemp)
            tvHour = v.findViewById(R.id.tvHour)
            ivIcon = v.findViewById(R.id.ivIcon)
        }

        fun bind(hourlyWeather: HourlyWeather, context: Context, position: Int) {
            tvTemp.text = hourlyWeather.temp + "°"
            if (position == 0)
            {
                tvHour.text = " Now "
            }
            else
            {
                tvHour.text = SimpleDateFormat(
                    "HH:mm",
                    Locale.getDefault()
                ).format(Date(hourlyWeather.dt * 1000))//hourlyWeather.dt
            }
            Log.d("DEBUG", "Icon:${hourlyWeather.icon}")

            Glide.with(context)
                .load("https://openweathermap.org/img/wn/${hourlyWeather.icon}@2x.png")
                //.dontAnimate()
                .placeholder(R.drawable.ic_round_image_not_supported_24)
                .error(R.drawable.ic_outline_error_outline_24)
                .centerCrop()
                .into(ivIcon)
        }

        override fun onClick(p0: View?) {

        }
    }

}