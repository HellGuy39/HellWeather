package com.hellguy39.hellweather.presentation.fragments.location_manager

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.LocationManagerFragmentBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.adapter.LocationsAdapter
import com.hellguy39.hellweather.presentation.adapter.NextDaysAdapter
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationManagerFragment : Fragment(R.layout.location_manager_fragment) {

    private lateinit var viewModel: LocationManagerViewModel
    private lateinit var binding: LocationManagerFragmentBinding
    private lateinit var fragView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).setToolbarTittle("Location manager")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragView = view
        binding = LocationManagerFragmentBinding.bind(view)
        viewModel = ViewModelProvider(this)[LocationManagerViewModel::class.java]

        viewModel.userLocations.observe(this, {
            updateRecycler(it)
        })

        binding.fabAdd.setOnClickListener {
            fragView.findNavController().navigate(R.id.action_locationManagerFragment_to_addCityFragment)
        }

    }

    private fun updateRecycler(list: List<UserLocation>) {
        binding.recyclerLocations.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = LocationsAdapter(context, list)
        }
    }

}