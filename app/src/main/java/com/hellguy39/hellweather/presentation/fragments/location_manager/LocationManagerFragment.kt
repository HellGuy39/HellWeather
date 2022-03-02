package com.hellguy39.hellweather.presentation.fragments.location_manager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.LocationManagerFragmentBinding
import com.hellguy39.hellweather.domain.models.param.UserLocationParam
import com.hellguy39.hellweather.domain.models.weather.WeatherData
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.activities.main.MainActivityViewModel
import com.hellguy39.hellweather.presentation.adapter.LocationsAdapter
import com.hellguy39.hellweather.utils.setToolbarNavigation
import com.hellguy39.hellweather.utils.shortSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class LocationManagerFragment : Fragment(R.layout.location_manager_fragment) {

    private lateinit var _mainViewModel: MainActivityViewModel
    private lateinit var _viewModel: LocationManagerViewModel
    private lateinit var _binding: LocationManagerFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel = ViewModelProvider(this)[LocationManagerViewModel::class.java]
        _mainViewModel = ViewModelProvider(activity as MainActivity)[MainActivityViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = LocationManagerFragmentBinding.bind(view)

        _binding.toolbar.setToolbarNavigation(toolbar = _binding.toolbar, activity = activity as MainActivity)

        _binding.recyclerLocations.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        _binding.fabAdd.setOnClickListener {
            findNavController()
                .navigate(LocationManagerFragmentDirections.actionLocationManagerFragmentToAddCityFragment())
        }

        setObservers()
    }

    private fun setObservers() {
        _mainViewModel.getWeatherDataList().observe(activity as MainActivity) { weatherDataList ->

            val locationsList = _mainViewModel.getUserLocationsList().value

            if (!locationsList.isNullOrEmpty() && !weatherDataList.isNullOrEmpty()) {
                updateRecycler(locationList = locationsList, weatherDataList = weatherDataList)
            } else if (!locationsList.isNullOrEmpty()){
                updateRecycler(locationList = locationsList)
            }

        }
    }

    private fun updateRecycler(
        locationList: List<UserLocationParam> = listOf(),
        weatherDataList: List<WeatherData> = listOf()
    ) {
        val units = _viewModel.getUnits()

        val adapter = LocationsAdapter(
            locationList = locationList,
            weatherDataList = weatherDataList,
            resources = resources,
            units = units
        )

        _binding.recyclerLocations.adapter = adapter

        val swipeGesture = object : SwipeGesture(requireContext(), resources) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                super.onSwiped(viewHolder, direction)

                val pos = viewHolder.bindingAdapterPosition

                deleteLocationItem(_mainViewModel.getUserLocationsList().value?.get(pos) ?: return)

                _binding.recyclerLocations.adapter?.notifyItemRemoved(pos)
            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(_binding.recyclerLocations)
    }

    private fun deleteLocationItem(userLocationParam: UserLocationParam) {
        _viewModel.onDeleteItem(userLocationParam)
        _mainViewModel.onRepositoryChanged()
        _binding.root.shortSnackBar(resources.getString(R.string.location_deleted))
    }

}