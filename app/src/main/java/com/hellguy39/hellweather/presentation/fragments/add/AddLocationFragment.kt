package com.hellguy39.hellweather.presentation.fragments.add

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Looper
import android.text.TextUtils
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
import com.hellguy39.hellweather.domain.models.param.UserLocationParam
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

                    checkLocation(Type.Coordinates,"",latitude,longitude)

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
        (activity as MainActivity).updateToolbarMenu(Selector.Disable)
        (activity as MainActivity).drawerControl(Selector.Disable)
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

        binding.etCity.doOnTextChanged { _, _, _, count ->
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
                State.Successful -> {
                    loadController(Selector.Disable)
                    val userLocationParam = viewModel.userLocationLive.value

                    if (userLocationParam != null) {
                        navigate(userLocationParam)
                        viewModel.clearData()
                    }
                }
                State.Error -> {
                    loadController(Selector.Disable)
                    fragView.shortSnackBar(viewModel.errorMessage.value!!)
                }
            }
        }

    }

    private fun checkLocation(type: Enum<Type>, cityName: String = "", lat: Double = 0.0, lon: Double = 0.0) {
        loadController(Selector.Enable)

        if (type == Type.CityName)
            viewModel.requestWithCityName(cityName)

        if (type == Type.Coordinates)
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

    private fun loadController(action: Enum<Selector>) {
            when (action) {
                Selector.Enable -> {
                    isLoading = true

                    binding.etCity.isEnabled = false
                    binding.fabNext.isEnabled = false
                    binding.btnFindMe.isEnabled = false
                    binding.progressLinear.visibility = View.VISIBLE
                }
                Selector.Disable -> {
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
                loadController(Selector.Enable)
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
                    loadController(Selector.Disable)
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
                    checkLocation(Type.CityName, input)
                }
                else
                {
                    fragView.shortSnackBar(resources.getString(R.string.not_funny))
                }
            }
        }
    }
}