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

class NextDaysAdapter(
    private val context: Context,
    private val dayList: MutableList<DailyWeather>
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
        holder.bind(dailyWeather, context)
    }

    override fun getItemCount(): Int = dayList.size

    class DayViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var tvMaxTemp: TextView
        private var tvMinTemp: TextView
        private var tvHumidity: TextView
        private var tvDay: TextView
        private var ivIcon : ImageView

        init {
            tvMaxTemp = v.findViewById(R.id.tvMaxTemp)
            tvMinTemp = v.findViewById(R.id.tvMinTemp)
            tvHumidity = v.findViewById(R.id.tvHumidity)
            tvDay = v.findViewById(R.id.tvDay)
            ivIcon = v.findViewById(R.id.ivIcon)
        }

        fun bind(dailyWeather: DailyWeather, context: Context) {
            tvMaxTemp.text = dailyWeather.max + "°"
            tvMinTemp.text = dailyWeather.min + "°"
            tvHumidity.text = dailyWeather.humidity + "%"
            tvDay.text = dailyWeather.dt

            Log.d("DEBUG", "Icon:${dailyWeather.icon}")

            Glide.with(context)
                .load("https://openweathermap.org/img/wn/${dailyWeather.icon}@2x.png")
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