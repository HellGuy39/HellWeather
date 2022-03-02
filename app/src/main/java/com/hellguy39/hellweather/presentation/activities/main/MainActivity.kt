package com.hellguy39.hellweather.presentation.activities.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.MainActivityBinding
import com.hellguy39.hellweather.domain.models.param.UserLocationParam
import com.hellguy39.hellweather.domain.usecase.prefs.service.ServiceUseCases
import com.hellguy39.hellweather.domain.utils.Prefs
import com.hellguy39.hellweather.presentation.fragments.home.HomeFragmentDirections
import com.hellguy39.hellweather.presentation.services.WeatherService
import com.hellguy39.hellweather.utils.Selector
import com.hellguy39.hellweather.utils.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var serviceUseCases: ServiceUseCases

    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var binding: MainActivityBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    private var firstBoot = false
    private var serviceMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        firstBoot = intent.getBooleanExtra(Prefs.FirstBoot.name, false)
        serviceMode = intent.getBooleanExtra(Prefs.ServiceMode.name, false)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.navigationView.setCheckedItem(R.id.homeFragment)
        NavigationUI.setupWithNavController(binding.navigationView, navController)

        setupMaterialShapeToDrawer()

        if (firstBoot) {
            if(isOnHomeFragment()) {
                navController.navigate(HomeFragmentDirections.actionHomeFragmentToWelcomeFragment())
            }
        }

        checkService()
        setObservers()
    }

    private fun setupMaterialShapeToDrawer() {
        val navViewBackground:MaterialShapeDrawable = binding.navigationView.background as MaterialShapeDrawable
        navViewBackground.shapeAppearanceModel = navViewBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(CornerFamily.ROUNDED, resources.getDimensionPixelSize(R.dimen.drawer_corner).toFloat())
            .setBottomRightCorner(CornerFamily.ROUNDED, resources.getDimensionPixelSize(R.dimen.drawer_corner).toFloat())
            .build()

    }

    private fun setObservers() {
        viewModel.getUserLocationsList().observe(this) {
            if (!viewModel.isInProgress()) {
                if (viewModel.getStatus().value != State.Empty) {
                    //val weatherDataList = viewModel.weatherDataListLive.value
                    viewModel.fetchWeather(it)
                    /*if (serviceMode) {
                        serviceControl(Selector.Reboot)
                    }*/
                }
            }
        }
    }

    private fun checkService() = CoroutineScope(Dispatchers.IO).launch {
        val serviceMode = serviceUseCases.getServiceModeUseCase.invoke()

        if (serviceMode && !WeatherService.isRunning()) {
            serviceControl(Selector.Enable)
        }
    }

    fun serviceControl(action: Enum<Selector>) {
        when(action) {
            Selector.Enable -> {
                if (!WeatherService.isRunning()) {
                    startService()
                }
            }
            Selector.Disable -> {
                if (WeatherService.isRunning()) {
                    WeatherService.stopService(this)
                }
            }
            Selector.Reboot -> {
                if (WeatherService.isRunning()) {
                    WeatherService.stopService(this)
                    startService()
                }
            }
        }
    }

    private fun startService() {
        val list = viewModel.getUserLocationsList().value

        if (!list.isNullOrEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                val locationStr = serviceUseCases.getServiceLocationUseCase.invoke()

                var userLocationPos = 0

                for (n in list.indices) {
                    if (locationStr == list[n].locationName)
                        userLocationPos = n
                }

                WeatherService.startService(this@MainActivity, list[userLocationPos])
            }
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isOpen)
        {
            closeDrawer()
        }
        else
        {
            super.onBackPressed()
        }
    }

    fun openDrawer() = binding.drawerLayout.open()

    private fun closeDrawer() = binding.drawerLayout.close()

    private fun isOnHomeFragment(): Boolean = navController.currentDestination?.id == R.id.homeFragment

}

