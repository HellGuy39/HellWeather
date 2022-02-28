package com.hellguy39.hellweather.presentation.fragments.location_manager

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.LocationManagerFragmentBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.activities.main.MainActivityViewModel
import com.hellguy39.hellweather.presentation.adapter.LocationsAdapter
import com.hellguy39.hellweather.domain.models.param.UserLocationParam
import com.hellguy39.hellweather.utils.Selector
import com.hellguy39.hellweather.utils.setToolbarNavigation
import com.hellguy39.hellweather.utils.shortSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationManagerFragment : Fragment(R.layout.location_manager_fragment) {

    private lateinit var mainViewModel: MainActivityViewModel
    private lateinit var viewModel: LocationManagerViewModel
    private lateinit var _binding: LocationManagerFragmentBinding
    private lateinit var fragView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LocationManagerViewModel::class.java]
        mainViewModel = ViewModelProvider(activity as MainActivity)[MainActivityViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragView = view
        _binding = LocationManagerFragmentBinding.bind(view)

        _binding.toolbar.setToolbarNavigation(toolbar = _binding.toolbar, activity = activity as MainActivity)

        setObservers()

        _binding.recyclerLocations.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        _binding.fabAdd.setOnClickListener {
            fragView.findNavController()
                .navigate(LocationManagerFragmentDirections.actionLocationManagerFragmentToAddCityFragment())
        }

    }

    private fun setObservers() {
        /*mainViewModel.userLocationsLive.observe(activity as MainActivity) {
            updateRecycler(it)
        }

        mainViewModel.weatherDataListLive.observe(activity as MainActivity) {
            val list = mainViewModel.userLocationsLive.value

            if (list != null)
                if (list.isNotEmpty())
                    updateRecycler(list)

        }*/
    }

    private fun updateRecycler(list: List<UserLocationParam>) {
        /*val weatherDataList = mainViewModel.weatherDataListLive.value ?: return

        val adapter = LocationsAdapter(
            locationList = list,
            weatherDataList = weatherDataList,
            resources = resources,
            units = viewModel.getUnits()
        )

        _binding.recyclerLocations.adapter = adapter

        val swipeGesture = object : SwipeGesture(requireContext(), resources) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                super.onSwiped(viewHolder, direction)

                val pos = viewHolder.bindingAdapterPosition

                deleteLocationItem(mainViewModel.getUserLocationsList()[pos])

                _binding.recyclerLocations.adapter?.notifyItemRemoved(pos)
            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(_binding.recyclerLocations)
*/
    }

    private fun deleteLocationItem(userLocationParam: UserLocationParam) {
        viewModel.onDeleteItem(userLocationParam)
        mainViewModel.onRepositoryChanged()
        fragView.shortSnackBar(resources.getString(R.string.location_deleted))
    }

}