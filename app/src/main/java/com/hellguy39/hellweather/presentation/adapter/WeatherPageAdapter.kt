package com.hellguy39.hellweather.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hellguy39.hellweather.presentation.fragments.page.WeatherPageFragment
import com.hellguy39.hellweather.repository.database.pojo.WeatherData

class WeatherPageAdapter(
    frag: Fragment,
    private val weatherDataList: List<WeatherData>
) : FragmentStateAdapter(frag) {

    override fun getItemCount(): Int = weatherDataList.size

    override fun createFragment(position: Int): WeatherPageFragment
        = WeatherPageFragment.newInstance(weatherDataList[position])
}