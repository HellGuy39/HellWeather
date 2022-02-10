package com.hellguy39.hellweather.presentation.fragments.confirmation

import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.ConfirmationCityFragmentBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.activities.main.MainActivityViewModel
import com.hellguy39.hellweather.presentation.fragments.location_manager.LocationManagerFragment
import com.hellguy39.hellweather.presentation.fragments.location_manager.LocationManagerFragmentDirections
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import com.hellguy39.hellweather.utils.DISABLE
import com.hellguy39.hellweather.utils.ENABLE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmationCityFragment : Fragment(R.layout.confirmation_city_fragment), View.OnClickListener {

    private lateinit var viewModel: ConfirmationCityViewModel
    private lateinit var mainViewModel: MainActivityViewModel
    private lateinit var binding: ConfirmationCityFragmentBinding
    private lateinit var userLocation: UserLocation
    private val args: ConfirmationCityFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ConfirmationCityViewModel::class.java]
        mainViewModel = ViewModelProvider(activity as MainActivity)[MainActivityViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).setToolbarTittle(getString(R.string.tittle_location_manager))
        (activity as MainActivity).updateToolbarMenu(DISABLE)
        (activity as MainActivity).drawerControl(DISABLE)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = ConfirmationCityFragmentBinding.bind(view)

        userLocation = args.userLocation
        updateUI(userLocation)
        binding.fabFalse.setOnClickListener(this)
        binding.fabTrue.setOnClickListener(this)

    }

    private fun updateUI(usrLoc: UserLocation) {
        binding.tvLocation.text = usrLoc.country + ", " + usrLoc.locationName
        binding.tvCoords.text = "Lat: ${usrLoc.lat} Lon: ${usrLoc.lon}"
        if (usrLoc.timezone == 0)
        {
            binding.tvTimezone.text = "${usrLoc.timezone} GMT"
        }
        else if (usrLoc.timezone > 0)
        {
            binding.tvTimezone.text = "+${usrLoc.timezone} GMT"
        }
        else
        {
            binding.tvTimezone.text = "${usrLoc.timezone} GMT"
        }

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.fabTrue -> {
                viewModel.saveToRoom(userLocation)
                mainViewModel.onRepositoryChanged()
                (activity as MainActivity).drawerControl(ENABLE)

                if (viewModel.isFirstBoot()) {
                    viewModel.disableFirstBoot()
                    navigateToHome(p0)
                } else {
                    navigateToLocationManager(p0)
                }
            }
            R.id.fabFalse -> {
                p0.findNavController().popBackStack()
            }
        }
    }

    private fun navigateToHome(v: View) = v.findNavController()
        .navigate(ConfirmationCityFragmentDirections.actionConfirmationCityFragmentToHomeFragment())


    private fun navigateToLocationManager(v: View) = v.findNavController()
        .navigate(ConfirmationCityFragmentDirections.actionConfirmationCityFragmentToLocationManagerFragment())
}