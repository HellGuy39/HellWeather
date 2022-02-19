package com.hellguy39.hellweather.presentation.fragments.add

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.gms.location.*
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentAddLocationBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.data.enteties.UserLocation
import com.hellguy39.hellweather.utils.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddLocationFragment : Fragment(R.layout.fragment_add_location), View.OnClickListener {

    private lateinit var binding: FragmentAddLocationBinding
    private lateinit var fragView: View
    private lateinit var viewModel: AddLocationViewModel
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var checkPermission: ActivityResultLauncher<Array<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AddLocationViewModel::class.java]
        locationRequest = createLocationRequest()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                if (p0.locations.size > 0) {
                    val index: Int = p0.locations.size - 1
                    val latitude: Double = p0.locations[index].latitude
                    val longitude: Double = p0.locations[index].longitude

                    checkLocation(TYPE_LAT_LON,"",latitude,longitude)

                    removeLocationUpdates()
                    //Log.d("DEBUG", "Lat:${latitude} % Lon:${longitude}")//viewModel.sendRequestWithLatLon(latitude, longitude)
                }
            }
        }
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
        savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        checkPermission = registerForActivityResult(ActivityResultContracts
            .RequestMultiplePermissions()) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    findMe()
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    findMe()
                } else -> {
                    fragView.shortSnackBar(resources.getString(R.string.access_denied))
                }
            }
            /*if (it[Manifest.permission.ACCESS_COARSE_LOCATION] == true ||
                    it[Manifest.permission.ACCESS_FINE_LOCATION] == true)
            {
                findMe()
            }
            else
            {
                fragView.shortSnackBar(resources.getString(R.string.access_denied))
            }*/
        }

        fragView = view
        binding = FragmentAddLocationBinding.bind(view)

        viewModel.userLocationLive.observe(viewLifecycleOwner) {
            if (it != null )
            {
                navigate(it)
                viewModel.clearData()
            }
        }

        viewModel.statusLive.observe(viewLifecycleOwner) {
            loadController(DISABLE)
            when (it) {
                SUCCESSFUL -> {
                    //navigate(viewModel.userLocationLive.value!!)
                    //viewModel.clearData() //strange solution
                }
                EMPTY_BODY -> {
                    fragView.shortSnackBar(resources.getString(R.string.city_not_found))
                }
                ERROR -> {
                    fragView.shortSnackBar(resources.getString(R.string.city_not_found))
                }
                FAILURE -> {
                    fragView.shortSnackBar(resources.getString(R.string.server_not_responding))
                }
            }
        }

        binding.btnFindMe.setOnClickListener(this)
        binding.fabNext.setOnClickListener(this)
    }

    private fun checkLocation(type: String, cityName: String = "", lat: Double = 0.0, lon: Double = 0.0) {
        loadController(ENABLE)
        viewModel.requestController(type,cityName,lat,lon)
    }

    private fun removeLocationUpdates() {
        LocationServices.getFusedLocationProviderClient(activity as MainActivity)
            .removeLocationUpdates(locationCallback)
    }

    private fun isGPSEnabled(): Boolean {
        val locationManager: LocationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun loadController(action: String) {
            when (action) {
                ENABLE -> {
                    binding.etCity.isEnabled = false
                    binding.fabNext.isEnabled = false
                    binding.btnFindMe.isEnabled = false
                    binding.progressLinear.visibility = View.VISIBLE
                }
                DISABLE -> {
                    binding.etCity.isEnabled = true
                    binding.fabNext.isEnabled = true
                    binding.btnFindMe.isEnabled = true
                    binding.progressLinear.visibility = View.INVISIBLE
                }
            }
    }

    private fun createLocationRequest() : LocationRequest {
        return LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
            .setInterval(5 * 1000)
            .setFastestInterval(2 * 1000)
    }

    private fun checkTextField(input: String) : Boolean = !TextUtils.isEmpty(input)


    private fun navigate(userLocation: UserLocation) = fragView.findNavController()
        .navigate(/*R.id.action_addCityFragment_to_confirmationCityFragment*/AddLocationFragmentDirections.actionAddCityFragmentToConfirmationCityFragment(userLocation))

    private fun findMe() {
        if (ActivityCompat.checkSelfPermission(activity as MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(activity as MainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if(isGPSEnabled())
            {
                loadController(ENABLE)

                LocationServices.getFusedLocationProviderClient(requireActivity())
                    .requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
            }
            else
            {
                fragView.shortSnackBar(resources.getString(R.string.gps_is_disabled))
            }

        } else {
            checkPermission?.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id)
        {
            binding.btnFindMe.id -> {
                findMe()
            }
            binding.fabNext.id -> {
                val input = binding.etCity.text.toString()

                if (checkTextField(input))
                {
                    checkLocation(TYPE_CITY_NAME, input)
                }
                else
                {
                    fragView.shortSnackBar(resources.getString(R.string.not_funny))
                }
            }
        }
    }
}