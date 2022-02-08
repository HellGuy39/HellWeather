package com.hellguy39.hellweather.presentation.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentHomeBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.activities.main.MainActivityViewModel
import com.hellguy39.hellweather.presentation.adapter.WeatherPageAdapter
import com.hellguy39.hellweather.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment() : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        mainActivityViewModel =
            ViewModelProvider(activity as MainActivity)[MainActivityViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).setToolbarTittle(getString(R.string.loading))
        (activity as MainActivity).updateToolbarMenu(ENABLE)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        refreshing(ENABLE)

        mainActivityViewModel.statusLive.observe(activity as MainActivity) {
            if (it == SUCCESSFUL) {
                refreshing(DISABLE)
                //val list = mainActivityViewModel.weatherJsonListLive.value
                val weatherDataList = mainActivityViewModel.weatherDataListLive.value
                val userLocations = mainActivityViewModel.userLocationsLive.value

                if (userLocations == null || userLocations.isEmpty())
                    return@observe

                if (weatherDataList == null || weatherDataList.isEmpty())
                    return@observe

                //Log.d("DEBUG", "W: ${weatherDataList.size}\nU: ${userLocations.size}")

                val pagerAdapter = WeatherPageAdapter(this, weatherDataList)
                binding.viewPager.adapter = pagerAdapter
                TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                    tab.text = userLocations[position].locationName

                    if (findNavController().currentDestination?.id == R.id.homeFragment) {
                        (activity as MainActivity).setToolbarTittle(
                            SimpleDateFormat("E, HH:mm", Locale.getDefault()).format(
                                Date(weatherDataList[position].currentWeather.dt * 1000)
                            )
                        )
                    }
                }.attach()
            }
            else if (it == FAILURE)
            {
                refreshing(DISABLE)
                (activity as MainActivity).setToolbarTittle("Connection lost")
            }
            else if (it == ERROR)
            {
                refreshing(DISABLE)
                (activity as MainActivity).setToolbarTittle("Error")
            }
        }
    }

    fun onRefresh() {
    }

    fun refreshing(action: String) {
        if (action == ENABLE) {
            binding.progressIndicator.visibility = View.VISIBLE
            (activity as MainActivity).setToolbarTittle(getString(R.string.loading))
        }
        else
        {
            binding.progressIndicator.visibility = View.INVISIBLE
        }
    }

    override fun onStart() {
        super.onStart()

    }
}