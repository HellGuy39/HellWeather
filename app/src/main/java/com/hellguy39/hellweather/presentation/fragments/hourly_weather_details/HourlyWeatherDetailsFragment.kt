package com.hellguy39.hellweather.presentation.fragments.hourly_weather_details

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentHourlyWeatherDetailsBinding
import com.hellguy39.hellweather.domain.model.HourlyWeather
import com.hellguy39.hellweather.format.DateFormatter
import com.hellguy39.hellweather.helpers.IconHelper
import com.hellguy39.hellweather.presentation.activities.main.SharedViewModel
import com.hellguy39.hellweather.presentation.adapter.DetailModel
import com.hellguy39.hellweather.presentation.adapter.DetailsAdapter
import com.hellguy39.hellweather.presentation.adapter.toDetailsModelList
import com.hellguy39.hellweather.utils.getColorFromAttr
import com.hellguy39.hellweather.utils.setImageAsync
import com.hellguy39.hellweather.utils.updateAndClearRecycler
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class HourlyWeatherDetailsFragment : Fragment(R.layout.fragment_hourly_weather_details) {

    private lateinit var binding: FragmentHourlyWeatherDetailsBinding
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
        binding = FragmentHourlyWeatherDetailsBinding.bind(view)
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
        sharedViewModel.getHourlyWeatherItem().observe(viewLifecycleOwner) {
            updateUI(it)
        }
    }

    private fun updateUI(hourlyWeather: HourlyWeather) {
        binding.run {
            tvTemp.text = hourlyWeather.temp?.roundToInt().toString()
            tvDate.text = DateFormatter.format(hourlyWeather.date, DateFormatter.DATE_OF_THE_MOUTH_AND_HOUR)
            tvWeatherDescription.text = hourlyWeather.weather?.get(0)?.description?.replaceFirstChar(Char::titlecase)
            tvTempFeelsLike.text = resources.getString(
                R.string.text_temp_feels_like,
                hourlyWeather.feelsLike?.roundToInt()
            )
            ivIcon.setImageAsync(IconHelper.getByIconId(hourlyWeather.weather?.get(0)))
            rvDetails.updateAndClearRecycler(detailsList, hourlyWeather.toDetailsModelList(resources))
        }
    }
}