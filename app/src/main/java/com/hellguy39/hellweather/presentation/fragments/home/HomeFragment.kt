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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var tabLayoutMediator: TabLayoutMediator

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
        //binding.viewPager.visibility = View.INVISIBLE
    }

    override fun onStart() {
        super.onStart()
        if(mainActivityViewModel.firstBootLive.value == false) {
            refreshing(ENABLE)
            setStatusObserver()
        }
    }

    private fun setStatusObserver() {
        mainActivityViewModel.statusLive.observe(activity as MainActivity) {
            if (it == null)
                return@observe

            when (it) {
                SUCCESSFUL -> {

                    refreshing(DISABLE)

                    val weatherDataList = mainActivityViewModel.weatherDataListLive.value
                    val userLocations = mainActivityViewModel.userLocationsLive.value

                    if (userLocations == null || userLocations.isEmpty())
                        return@observe

                    if (weatherDataList == null || weatherDataList.isEmpty())
                        return@observe

                    CoroutineScope(Dispatchers.Main).launch {

                        val pagerAdapter = WeatherPageAdapter(this@HomeFragment, weatherDataList)
                        binding.viewPager.adapter = pagerAdapter
                        tabLayoutMediator = TabLayoutMediator(
                            binding.tabLayout,
                            binding.viewPager
                        ) { tab, position ->
                            tab.text = userLocations[position].locationName

                            if (findNavController().currentDestination?.id == R.id.homeFragment) {
                                (activity as MainActivity).setToolbarTittle(
                                    SimpleDateFormat("E, HH:mm", Locale.getDefault()).format(
                                        Date(weatherDataList[position].currentWeather.dt * 1000)
                                    )
                                )
                            }
                        }
                        tabLayoutMediator.attach()
                        //animateViewPager()
                    }
                }
                FAILURE -> {
                    refreshing(DISABLE)

                    if (findNavController().currentDestination?.id == R.id.homeFragment)
                        (activity as MainActivity).setToolbarTittle(resources.getString(R.string.connection_lost))
                }
                ERROR -> {
                    refreshing(DISABLE)

                    if (findNavController().currentDestination?.id == R.id.homeFragment)
                        (activity as MainActivity).setToolbarTittle(resources.getString(R.string.error))
                }
                IN_PROGRESS -> {
                    refreshing(ENABLE)
                }
                EMPTY_LIST -> {
                    refreshing(DISABLE)

                    if (findNavController().currentDestination?.id == R.id.homeFragment)
                        (activity as MainActivity).setToolbarTittle(resources.getString(R.string.no_locations))
                }
                /*EXPECTATION -> {
                    refreshing(ENABLE)
                }*/
            }
        }
    }

    private fun animateViewPager() {
        binding.viewPager.apply {
            alpha = 0f
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (this::tabLayoutMediator.isInitialized) {
            if (tabLayoutMediator.isAttached) {
                tabLayoutMediator.detach()
            }
        }
        //mainActivityViewModel.statusLive.removeObservers(activity as MainActivity)
    }


    private fun refreshing(action: String) {
        if (findNavController().currentDestination?.id != R.id.homeFragment)
            return

        if (action == ENABLE) {
            binding.progressIndicator.visibility = View.VISIBLE
            (activity as MainActivity).setToolbarTittle(getString(R.string.loading))
        }
        else
        {
            binding.progressIndicator.visibility = View.INVISIBLE
        }
    }
}