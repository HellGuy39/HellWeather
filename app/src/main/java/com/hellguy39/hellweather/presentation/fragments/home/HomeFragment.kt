package com.hellguy39.hellweather.presentation.fragments.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentHomeBinding
import com.hellguy39.hellweather.domain.models.weather.WeatherData
import com.hellguy39.hellweather.domain.usecase.prefs.units.UnitsUseCases
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.activities.main.MainActivityViewModel
import com.hellguy39.hellweather.presentation.adapter.ErrorPageAdapter
import com.hellguy39.hellweather.presentation.adapter.WeatherPageAdapter
import com.hellguy39.hellweather.utils.Selector
import com.hellguy39.hellweather.utils.State
import dagger.hilt.android.AndroidEntryPoint
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
        if(mainActivityViewModel.getFirstBootValue().value == false) {
            refreshing(Selector.Enable)
            setStatusObserver()
        }
    }

    private fun setStatusObserver() {
        mainActivityViewModel.getStatus().observe(activity as MainActivity) {
            when (it) {
                State.Successful -> {

                    refreshing(Selector.Disable)

                    val weatherDataList = mainActivityViewModel.getWeatherDataList().value
                    val userLocations = mainActivityViewModel.getUserLocationsList().value

                    if (userLocations.isNullOrEmpty())
                        return@observe

                    if (weatherDataList.isNullOrEmpty())
                        return@observe

                    setupPagerAdapter(weatherDataList)

                    if (weatherDataList.size > 1) {
                        tabLayoutMediator = TabLayoutMediator(
                            binding.tabLayout,
                            binding.viewPager
                        ) { tab, position ->
                        }

                        tabLayoutMediator.attach()
                        //animateViewPager()
                    }
                }

                State.Error -> {
                    refreshing(Selector.Disable)
                    val errorMessage = mainActivityViewModel.getErrorMessage().value

                    val pagerAdapter = ErrorPageAdapter(
                        frag = this@HomeFragment,
                        errorMessage = errorMessage!!
                    )

                    binding.viewPager.adapter = pagerAdapter

                }
                State.Progress -> {
                    refreshing(Selector.Enable)
                }

                State.Empty -> {
                    refreshing(Selector.Disable)
                }
            }
        }
    }

    private fun setupPagerAdapter(receivedWeather: List<WeatherData>) {
        val pagerAdapter = WeatherPageAdapter(
            frag = this@HomeFragment,
            weatherDataList = receivedWeather,
            units = unitsUseCases.getUnitsUseCase.invoke()
        )

        binding.viewPager.adapter = pagerAdapter
    }

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
        } else {
            binding.progressIndicator.visibility = View.INVISIBLE
        }
    }

    private fun isOnHomeFragment(): Boolean = findNavController().currentDestination?.id == R.id.homeFragment
}