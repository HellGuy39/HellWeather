package com.hellguy39.hellweather.presentation.activities.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.MainActivityBinding
import com.hellguy39.hellweather.presentation.fragments.home.HomeFragmentDirections
import com.hellguy39.hellweather.presentation.services.WeatherService
import com.hellguy39.hellweather.data.enteties.UserLocation
import com.hellguy39.hellweather.domain.models.UserLocationParam
import com.hellguy39.hellweather.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MenuItem.OnMenuItemClickListener {

    //private val locationManagerViewModel : LocationManagerViewModel by viewModels()
    private lateinit var drawerLayout: DrawerLayout

    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var binding: MainActivityBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private  var toolBarMenu: Menu? = null

    private var _firstBoot = false
    private var _serviceMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        toolBarMenu = binding.topAppBar.menu

        _firstBoot = intent.getBooleanExtra(PREFS_FIRST_BOOT, false)
        _serviceMode = intent.getBooleanExtra(PREFS_SERVICE_MODE, false)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        /*toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()*/

        binding.topAppBar.setNavigationOnClickListener {
            openDrawer()
        }

        binding.topAppBar.menu.getItem(0).setOnMenuItemClickListener(this)

        binding.navigationView.setCheckedItem(R.id.homeFragment)
        NavigationUI.setupWithNavController(binding.navigationView, navController)
        /*setSupportActionBar(binding.topAppBar)
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)*/

        val navViewBackground:MaterialShapeDrawable = binding.navigationView.background as MaterialShapeDrawable
        navViewBackground.shapeAppearanceModel = navViewBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(CornerFamily.ROUNDED, resources.getDimensionPixelSize(R.dimen.drawer_corner).toFloat())
            .setBottomRightCorner(CornerFamily.ROUNDED, resources.getDimensionPixelSize(R.dimen.drawer_corner).toFloat())
            .build()

        if (_firstBoot)
        {
            drawerControl(DISABLE)

            if(navController.currentDestination?.id == R.id.homeFragment)
            {
                navController.navigate(HomeFragmentDirections.actionHomeFragmentToWelcomeFragment())
            }
        }

        viewModel.userLocationsLive.observe(this) {
            if (viewModel.statusLive.value != IN_PROGRESS) {
                if (it.isNullOrEmpty()) {
                    viewModel.statusLive.value = EMPTY_LIST
                    return@observe
                }

                val weatherDataList = viewModel.weatherDataListLive.value

                if (weatherDataList == null) {
                    //Log.d("DEBUG", "HERE 1")
                    updateData(it)
                }

                if (weatherDataList != null) {
                    if (weatherDataList.isEmpty()) {
                        //Log.d("DEBUG", "HERE 2")
                        updateData(it)
                    }
                }
            }
        }
    }

    private fun updateData(list: List<UserLocationParam>) {

        if (viewModel.statusLive.value != IN_PROGRESS) {
            viewModel.statusLive.value = IN_PROGRESS

            if (list.isNotEmpty()) {
                viewModel.loadAllLocation(list)
                if (_serviceMode) {
                    serviceControl(REBOOT)
                }
            }
            else
            {
                viewModel.statusLive.value = EMPTY_LIST
            }
        }
    }

    fun serviceControl(action: String) {
        if (action == ENABLE) {
            val list = viewModel.userLocationsLive.value
            if (!list.isNullOrEmpty()) {

                val locationStr = PreferenceManager.getDefaultSharedPreferences(this)
                    .getString(PREFS_SERVICE_LOCATION, NONE)

                var userLocationPos = 0

                for (n in list.indices) {
                    if (locationStr == list[n].locationName)
                        userLocationPos = n
                }

                //WeatherService.startService(this, list[userLocationPos])
            }
        }
        else if (action == REBOOT) {
            if (!WeatherService.isRunning()) {
                serviceControl(ENABLE)
            }
        }
        else {
            if (WeatherService.isRunning()) {
                WeatherService.stopService(this)
            }
        }
    }

    fun setToolbarTittle(s: String) {
        binding.topAppBar.title = s
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun drawerControl(action: String) {
        when(action) {
            ENABLE -> {
                binding.topAppBar.setNavigationOnClickListener {
                    openDrawer()
                }
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
            DISABLE -> {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                binding.topAppBar.setNavigationOnClickListener {
                    //Nothing
                }
            }
        }
    }

    fun openDrawer() = binding.drawerLayout.open()
    fun closeDrawer() = binding.drawerLayout.close()

    override fun onBackPressed() {
        if (binding.drawerLayout.isOpen)
        {
            binding.drawerLayout.close()
        }
        else
        {
            super.onBackPressed()
        }
    }

    fun updateToolbarMenu(action: String) {
        if (toolBarMenu != null) {
            if (action == ENABLE) {
                toolBarMenu!!.findItem(R.id.update).isVisible = true
            } else {
                toolBarMenu!!.findItem(R.id.update).isVisible = false
            }
        }
    }

    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        when (p0?.itemId) {
            R.id.update -> {
                if (navController.currentDestination?.id == R.id.homeFragment) {
                    //(navHostFragment.childFragmentManager.fragments[0] as HomeFragment).onRefresh()
                    val list = viewModel.userLocationsLive.value
                    if (list != null)
                        updateData(list)
                }
            }
        }
        return true
    }


}

