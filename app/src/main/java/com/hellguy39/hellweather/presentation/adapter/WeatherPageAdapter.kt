package com.hellguy39.hellweather.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.gson.JsonObject
import com.hellguy39.hellweather.presentation.fragments.page.WeatherPageFragment

class WeatherPageAdapter(
    fa: FragmentActivity,
    private val weatherList: List<JsonObject>
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = weatherList.size

    override fun createFragment(position: Int): Fragment = WeatherPageFragment(weatherList[position])
}