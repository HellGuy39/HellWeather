package com.hellguy39.hellweather.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hellguy39.hellweather.presentation.fragments.page.ErrorPageFragment

class ErrorPageAdapter(
    frag: Fragment,
    private val errorMessage: String
) : FragmentStateAdapter(frag) {

    override fun getItemCount(): Int = 1

    override fun createFragment(
        position: Int
    ): ErrorPageFragment = ErrorPageFragment.newInstance(errorMessage = errorMessage)
}