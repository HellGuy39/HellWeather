package com.hellguy39.hellweather.presentation.fragments.location_manager

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.LocationManagerFragmentBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.activities.main.MainActivityViewModel
import com.hellguy39.hellweather.presentation.adapter.LocationsAdapter
import com.hellguy39.hellweather.presentation.adapter.LocationsWithoutWeatherAdapter
import com.hellguy39.hellweather.presentation.fragments.add.AddLocationFragment
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import com.hellguy39.hellweather.utils.DISABLE
import com.hellguy39.hellweather.utils.ENABLE
import com.hellguy39.hellweather.utils.shortSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationManagerFragment : Fragment(R.layout.location_manager_fragment), LocationsAdapter.EventListener {

    private lateinit var mainViewModel: MainActivityViewModel
    private lateinit var viewModel: LocationManagerViewModel
    private lateinit var binding: LocationManagerFragmentBinding
    private lateinit var fragView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LocationManagerViewModel::class.java]
        mainViewModel = ViewModelProvider(activity as MainActivity)[MainActivityViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).setToolbarTittle(getString(R.string.tittle_location_manager))
        (activity as MainActivity).updateToolbarMenu(DISABLE)
        (activity as MainActivity).drawerControl(ENABLE)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragView = view
        binding = LocationManagerFragmentBinding.bind(view)

        mainViewModel.userLocationsLive.observe(activity as MainActivity) {
            if (it.isNotEmpty())
                updateRecycler(it)
        }

        mainViewModel.weatherDataListLive.observe(activity as MainActivity) {
            val list = mainViewModel.userLocationsLive.value

            if (list != null)
                if (list.isNotEmpty())
                    updateRecycler(list)

        }

        binding.fabAdd.setOnClickListener {
            fragView.findNavController()
                .navigate(LocationManagerFragmentDirections.actionLocationManagerFragmentToAddCityFragment())
        }

    }

    private fun updateRecycler(list: List<UserLocation>) {
        val weatherDataList = mainViewModel.weatherDataListLive.value

        if (weatherDataList == null)
            return

        if (weatherDataList.isNotEmpty()) {
            binding.recyclerLocations.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = LocationsAdapter(context, list, weatherDataList ,this@LocationManagerFragment)
            }
        }
        else
        {
            binding.recyclerLocations.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = LocationsWithoutWeatherAdapter(context, list)
            }
        }
    }

    override fun onDelete(userLocation: UserLocation) {
        viewModel.onDeleteItem(userLocation)
        mainViewModel.onRepositoryChanged()
        fragView.shortSnackBar("Item deleted")
    }

    override fun onEdit(userLocation: UserLocation) {
        //No way
    }

}