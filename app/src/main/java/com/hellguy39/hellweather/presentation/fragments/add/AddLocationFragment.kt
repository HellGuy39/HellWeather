package com.hellguy39.hellweather.presentation.fragments.add

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.gms.location.*
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentAddLocationBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.data.enteties.UserLocation
import com.hellguy39.hellweather.domain.models.UserLocationParam
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
    private val animationHelper = AnimationHelper()

    private var isLoading = false

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
        }

        fragView = view
        binding = FragmentAddLocationBinding.bind(view)

        binding.btnFindMe.setOnClickListener(this)
        binding.fabNext.setOnClickListener(this)
        animationHelper.exFabBottomOut(binding.fabNext) //Need to init animation

        binding.etCity.doOnTextChanged { text, start, before, count ->
            if (count > 0) {
                if (binding.fabNext.visibility == View.GONE)
                {
                    animationHelper.exFabBottomIn(binding.fabNext)
                }
            }
            else
            {
                if (binding.fabNext.visibility == View.VISIBLE)
                {
                    animationHelper.exFabBottomOut(binding.fabNext)
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()

        setObservers()
    }

    private fun setObservers() {
        viewModel.statusLive.observe(viewLifecycleOwner) {
            when (it) {
                SUCCESSFUL -> {
                    loadController(DISABLE)
                    val userLocationParam = viewModel.userLocationLive.value

                    if (userLocationParam != null) {
                        navigate(userLocationParam)
                        viewModel.clearData()
                    }
                }
                EMPTY_BODY -> {
                    loadController(DISABLE)
                    fragView.shortSnackBar(resources.getString(R.string.city_not_found))
                }
                ERROR -> {
                    loadController(DISABLE)
                    fragView.shortSnackBar(resources.getString(R.string.city_not_found))
                }
                FAILURE -> {
                    loadController(DISABLE)
                    fragView.shortSnackBar(resources.getString(R.string.server_not_responding))
                }
            }
        }

    }

    private fun checkLocation(type: String, cityName: String = "", lat: Double = 0.0, lon: Double = 0.0) {
        loadController(ENABLE)

        if (type == TYPE_CITY_NAME)
            viewModel.requestWithCityName(cityName)

        if (type == TYPE_LAT_LON)
            viewModel.requestWithCoordinates(lat, lon)
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
                    isLoading = true

                    binding.etCity.isEnabled = false
                    binding.fabNext.isEnabled = false
                    binding.btnFindMe.isEnabled = false
                    binding.progressLinear.visibility = View.VISIBLE
                }
                DISABLE -> {
                    isLoading = false

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


    private fun navigate(userLocationParam: UserLocationParam) = fragView.findNavController()
        .navigate(AddLocationFragmentDirections.actionAddCityFragmentToConfirmationCityFragment(userLocationParam))

    private fun findMe() {
        if (ActivityCompat.checkSelfPermission(activity as MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(activity as MainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if(isGPSEnabled())
            {
                loadController(ENABLE)
                LocationServices.getFusedLocationProviderClient(requireActivity())
                    .requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
                setRequestTimer()
            }
            else
            {
                fragView.shortSnackBar(resources.getString(R.string.gps_is_disabled))
            }

        } else {
            checkPermission?.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    private fun setRequestTimer() {
        val timer = object: CountDownTimer(10 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                if (isLoading) {
                    LocationServices.getFusedLocationProviderClient(requireActivity())
                        .removeLocationUpdates(locationCallback)
                    fragView.shortSnackBar(resources.getString(R.string.failed_to_find_device_location))
                    loadController(DISABLE)
                }
            }
        }
        timer.start()
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