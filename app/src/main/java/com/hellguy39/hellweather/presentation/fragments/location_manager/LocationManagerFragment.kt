package com.hellguy39.hellweather.presentation.fragments.location_manager

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.LocationManagerFragmentBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.adapter.LocationsAdapter
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import com.hellguy39.hellweather.utils.DISABLE
import com.hellguy39.hellweather.utils.shortSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationManagerFragment : Fragment(R.layout.location_manager_fragment), LocationsAdapter.EventListener {

    private lateinit var viewModel: LocationManagerViewModel
    private lateinit var binding: LocationManagerFragmentBinding
    private lateinit var fragView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LocationManagerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).setToolbarTittle(getString(R.string.tittle_location_manager))
        (activity as MainActivity).updateToolbarMenu(DISABLE)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragView = view
        binding = LocationManagerFragmentBinding.bind(view)

        viewModel.userLocations.observe(viewLifecycleOwner) {
            updateRecycler(it)
        }

        binding.fabAdd.setOnClickListener {
            fragView.findNavController().navigate(R.id.action_locationManagerFragment_to_addCityFragment)
        }

    }

    private fun updateRecycler(list: List<UserLocation>) {
        binding.recyclerLocations.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = LocationsAdapter(context, list, this@LocationManagerFragment)
        }
    }

    override fun onDelete(userLocation: UserLocation) {
        if (userLocation.id != 1) {
            viewModel.onDeleteItem(userLocation)
            fragView.shortSnackBar("Item deleted")
        }
        else
        {
            fragView.shortSnackBar("You can't delete your main location")
        }
    }

    override fun onEdit(userLocation: UserLocation) {
        Log.d("DEBUG", "HERE")
    }

}