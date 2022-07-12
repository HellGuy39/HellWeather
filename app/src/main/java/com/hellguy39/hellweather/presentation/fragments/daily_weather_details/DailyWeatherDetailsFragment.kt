package com.hellguy39.hellweather.presentation.fragments.daily_weather_details

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentDailyWeatherDetailsBinding
import com.hellguy39.hellweather.domain.model.DailyWeather
import com.hellguy39.hellweather.helpers.IconHelper
import com.hellguy39.hellweather.presentation.activities.main.SharedViewModel
import com.hellguy39.hellweather.presentation.adapter.DetailModel
import com.hellguy39.hellweather.presentation.adapter.DetailsAdapter
import com.hellguy39.hellweather.presentation.adapter.toDetailsModelList
import com.hellguy39.hellweather.utils.formatAsTitleDate
import com.hellguy39.hellweather.utils.getColorFromAttr
import com.hellguy39.hellweather.utils.setImageAsync
import com.hellguy39.hellweather.utils.updateAndClearRecycler
import kotlin.math.roundToInt

class DailyWeatherDetailsFragment : Fragment(R.layout.fragment_daily_weather_details) {

    private lateinit var binding: FragmentDailyWeatherDetailsBinding

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private val detailsList = mutableListOf<DetailModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(getColorFromAttr(R.attr.colorSurface))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDailyWeatherDetailsBinding.bind(view)
        binding.run {
            btnClose.setOnClickListener {
                findNavController().popBackStack()
            }
            rvDetails.apply {
                adapter = DetailsAdapter(
                    dataSet = detailsList,
                    resources = resources
                )
                layoutManager = GridLayoutManager(context, DetailsAdapter.SPAN_COUNT)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        sharedViewModel.getDailyWeatherItem().observe(viewLifecycleOwner) {
            updateUI(it)
        }
    }

    private fun updateUI(dailyWeather: DailyWeather) {
        binding.run {
            tvDate.text = dailyWeather.date?.formatAsTitleDate()
            ivIcon.setImageAsync(IconHelper.getByIconId(dailyWeather.weather?.get(0)))
            rvDetails.updateAndClearRecycler(detailsList, dailyWeather.toDetailsModelList(resources))

            tvTempEve.text = resources.getString(R.string.text_temp_eve, dailyWeather.temp?.eve?.roundToInt())
            tvTempMorn.text = resources.getString(R.string.text_temp_morn, dailyWeather.temp?.morn?.roundToInt())
            tvTempMax.text = resources.getString(R.string.text_temp_max, dailyWeather.temp?.max?.roundToInt())
            tvTempMin.text = resources.getString(R.string.text_temp_min, dailyWeather.temp?.min?.roundToInt())
            tvTempDay.text = resources.getString(R.string.text_temp_day, dailyWeather.temp?.day?.roundToInt())
            tvTempNight.text = resources.getString(R.string.text_temp_night, dailyWeather.temp?.night?.roundToInt())
            tvTempDay.text = resources.getString(R.string.text_temp_day, dailyWeather.temp?.day?.roundToInt())

            tvTempFeelsDay.text = resources.getString(R.string.text_temp_day, dailyWeather.feelsLike?.day?.roundToInt())
            tvTempFeelsEve.text = resources.getString(R.string.text_temp_eve, dailyWeather.feelsLike?.eve?.roundToInt())
            tvTempFeelsMorn.text = resources.getString(R.string.text_temp_morn, dailyWeather.feelsLike?.morn?.roundToInt())
            tvTempFeelsNight.text = resources.getString(R.string.text_temp_night, dailyWeather.feelsLike?.night?.roundToInt())
        }
    }
}