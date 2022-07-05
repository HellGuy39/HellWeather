package com.hellguy39.hellweather.presentation.fragments.location

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentLocationBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.activities.main.MainActivityViewModel
import com.hellguy39.hellweather.utils.PermissionState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.job

class LocationFragment : Fragment(R.layout.fragment_location) {

    private lateinit var binding: FragmentLocationBinding
    private val activityViewModel by activityViewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLocationBinding.bind(view)

        binding.fabGrantAccess.setOnClickListener {
            (activity as MainActivity).requestPermission()

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                activityViewModel.permissionState.collect {
                    when(it) {
                        PermissionState.Granted -> onAccessGranted()
                        PermissionState.Denied -> onAccessDenied()
                        else -> Unit
                    }
                }
            }
        }

//        Snackbar.make(binding.root, "Geolocation is disabled", Snackbar.LENGTH_LONG)
//            .setAction("Open settings") {
//                openLocationSettings()
//            }
//            .show()
    }

    private fun onAccessGranted() {
        Snackbar.make(binding.root, "Permission granted", Snackbar.LENGTH_LONG).show()
        findNavController().popBackStack()
    }

    private fun onAccessDenied() {
        Snackbar.make(binding.root, "Permission denied", Snackbar.LENGTH_LONG).show()
    }
}