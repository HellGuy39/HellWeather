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
import com.hellguy39.hellweather.domain.usecase.prefs.units.UnitsUseCases
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.activities.main.MainActivityViewModel
import com.hellguy39.hellweather.presentation.adapter.ErrorPageAdapter
import com.hellguy39.hellweather.presentation.adapter.WeatherPageAdapter
import com.hellguy39.hellweather.utils.Selector
import com.hellguy39.hellweather.utils.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    @Inject
    lateinit var unitsUseCases: UnitsUseCases

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
    }

    override fun onStart() {
        super.onStart()
        /*if(mainActivityViewModel.firstBootLive.value == false) {
            refreshing(Selector.Enable)
            setStatusObserver()
        }*/
    }

    /*private fun setStatusObserver() {
        mainActivityViewModel.statusLive.observe(activity as MainActivity) {
            when (it) {
                State.Successful -> {

                    refreshing(Selector.Disable)

                    val weatherDataList = mainActivityViewModel.weatherDataListLive.value
                    val userLocations = mainActivityViewModel.userLocationsLive.value

                    if (userLocations == null || userLocations.isEmpty())
                        return@observe

                    if (weatherDataList == null || weatherDataList.isEmpty())
                        return@observe

                    CoroutineScope(Dispatchers.Main).launch {

                        val pagerAdapter = WeatherPageAdapter(
                            frag = this@HomeFragment,
                            weatherDataList = weatherDataList,
                            units = unitsUseCases.getUnitsUseCase.invoke()
                        )

                        binding.viewPager.adapter = pagerAdapter

                        tabLayoutMediator = TabLayoutMediator(
                            binding.tabLayout,
                            binding.viewPager
                        ) { tab, position ->
                            tab.text = userLocations[position].locationName

                            *//*if (isOnHomeFragment()) {
                                (activity as MainActivity).setToolbarTittle(
                                    SimpleDateFormat("E, HH:mm", Locale.getDefault()).format(
                                        Date(weatherDataList[position].currentWeather.dt * 1000)
                                    )
                                )
                            }*//*
                        }
                        tabLayoutMediator.attach()
                        //animateViewPager()
                    }
                }

                State.Error -> {
                    refreshing(Selector.Disable)
                    val errorMessage = mainActivityViewModel.errorMessage.value

                    val pagerAdapter = ErrorPageAdapter(
                        frag = this@HomeFragment,
                        errorMessage = errorMessage!!
                    )

                    binding.viewPager.adapter = pagerAdapter

                    *//*if (isOnHomeFragment())
                        (activity as MainActivity).setToolbarTittle(errorMessage)*//*
                }
                State.Progress -> {
                    refreshing(Selector.Enable)
                }

                State.Empty -> {
                    refreshing(Selector.Disable)

                    *//*if (isOnHomeFragment())
                        (activity as MainActivity).setToolbarTittle(resources.getString(R.string.no_locations))*//*
                }
            }
        }
    }*/

    /*private fun animateViewPager() {
        binding.viewPager.apply {
            alpha = 0f
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(null)
        }
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        if (this::tabLayoutMediator.isInitialized) {
            if (tabLayoutMediator.isAttached) {
                tabLayoutMediator.detach()
            }
        }
    }


    private fun refreshing(action: Enum<Selector>) {
        if (!isOnHomeFragment())
            return

        if (action == Selector.Enable) {
            binding.progressIndicator.visibility = View.VISIBLE
            /*(activity as MainActivity).setToolbarTittle(getString(R.string.loading))*/
        }
        else
        {
            binding.progressIndicator.visibility = View.INVISIBLE
        }
    }

    private fun isOnHomeFragment(): Boolean = findNavController().currentDestination?.id == R.id.homeFragment
}