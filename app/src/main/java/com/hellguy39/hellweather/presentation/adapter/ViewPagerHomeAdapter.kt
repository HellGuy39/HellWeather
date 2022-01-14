package com.hellguy39.hellweather.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.repository.database.pojo.UserLocation


class ViewPagerHomeAdapter(
    private val size: Int,
    private val tittle: String
) : PagerAdapter() {

    override fun getCount(): Int = size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getPageTitle(position: Int): CharSequence {
        return tittle
    }

}
