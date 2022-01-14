package com.hellguy39.hellweather.presentation.fragments.confirmation

import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.ConfirmationCityFragmentBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import com.hellguy39.hellweather.utils.ENABLE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmationCityFragment : Fragment(R.layout.confirmation_city_fragment), View.OnClickListener {

    private lateinit var viewModel: ConfirmationCityViewModel
    private lateinit var binding: ConfirmationCityFragmentBinding
    private lateinit var userLocation: UserLocation
    private val args: ConfirmationCityFragmentArgs by navArgs()
    private lateinit var fragView: View

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        fragView = view
        viewModel = ViewModelProvider(this)[ConfirmationCityViewModel::class.java]
        binding = ConfirmationCityFragmentBinding.bind(view)

        userLocation = args.userLocation
        updateUI(userLocation)
        binding.fabFalse.setOnClickListener(this)
        binding.fabTrue.setOnClickListener(this)

    }

    private fun updateUI(usrLoc: UserLocation) {
        binding.tvCity.text = usrLoc.locationName
        binding.tvRegion.text = usrLoc.country
        binding.tvCoords.text = "Lat: ${usrLoc.lat} Lon: ${usrLoc.lon}"
        if (usrLoc.timezone > 0)
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
                disableFirstBoot()
                viewModel.saveToRoom(userLocation)
                navigate()
                (activity as MainActivity).drawerControl(ENABLE)
            }
            R.id.fabFalse -> {
                //Log.d("DEBUG", )
                fragView.findNavController().popBackStack()
            }
        }
    }

    private fun navigate() = fragView.findNavController()
        .navigate(ConfirmationCityFragmentDirections.actionConfirmationCityFragmentToHomeFragment())

    private fun disableFirstBoot() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val edit: SharedPreferences.Editor = sharedPreferences.edit()
        edit.putBoolean("first_boot", false)
        edit.apply()
    }

}