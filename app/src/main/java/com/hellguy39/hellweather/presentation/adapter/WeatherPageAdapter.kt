package com.hellguy39.hellweather.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hellguy39.hellweather.presentation.fragments.page.WeatherPageFragment
import com.hellguy39.hellweather.utils.WeatherData

class WeatherPageAdapter(
    fa: FragmentActivity,
    private val weatherDataList: List<WeatherData>
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = weatherDataList.size

    override fun createFragment(position: Int): Fragment = WeatherPageFragment(weatherDataList[position])
}