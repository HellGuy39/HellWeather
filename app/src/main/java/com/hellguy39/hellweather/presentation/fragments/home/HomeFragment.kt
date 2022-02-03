package com.hellguy39.hellweather.presentation.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentHomeBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.activities.main.MainActivityViewModel
import com.hellguy39.hellweather.presentation.adapter.WeatherPageAdapter
import com.hellguy39.hellweather.utils.ENABLE
import com.hellguy39.hellweather.utils.SUCCESSFUL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

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

        mainActivityViewModel.statusLive.observe(activity as MainActivity) {
            if (it == SUCCESSFUL) {

                val list = mainActivityViewModel.weatherJsonListLive.value
                val userLocations = mainActivityViewModel.userLocationsLive.value

                if (list != null) {
                    val pagerAdapter = WeatherPageAdapter(activity as MainActivity, list)
                    binding.viewPager.adapter = pagerAdapter
                    TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                        tab.text =
                            userLocations?.get(position)?.locationName
                        (activity as MainActivity).setToolbarTittle(getString(R.string.loading))
                    }.attach()
                }
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

        }
    }

    override fun onStart() {
        super.onStart()

    }
}