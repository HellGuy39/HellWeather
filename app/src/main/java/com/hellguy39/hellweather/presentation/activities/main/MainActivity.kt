package com.hellguy39.hellweather.presentation.activities.main

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

    }
}

//
//@AndroidEntryPoint
//class MainActivity : AppCompatActivity() {
//
//    @Inject
//    lateinit var serviceUseCases: ServiceUseCases
//
//    @Inject
//    lateinit var themeUseCases: ThemeUseCases
//
//    @Inject
//    lateinit var themeModeUseCases: ThemeModeUseCases
//
//    private val viewModel: MainActivityViewModel by viewModels()
//
//    private lateinit var binding: MainActivityBinding
//    private lateinit var navController: NavController
//    private lateinit var navHostFragment: NavHostFragment
//
//    private var firstBoot = false
//    private var serviceMode = false
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        //setupTheme()
//        binding = MainActivityBinding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)
//
//        firstBoot = intent.getBooleanExtra(Prefs.FirstBoot.name, false)
//        serviceMode = intent.getBooleanExtra(Prefs.ServiceMode.name, false)
//
//        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        navController = navHostFragment.navController
//
//        binding.navigationView.setCheckedItem(R.id.homeFragment)
//        NavigationUI.setupWithNavController(binding.navigationView, navController)
//
//        //setupMaterialShapeToDrawer()
//
//        if (firstBoot) {
//            if(isOnHomeFragment()) {
//                navController.navigate(HomeFragmentDirections.actionHomeFragmentToWelcomeFragment())
//            }
//        }
//
//        setObservers()
//    }

//    private fun setupTheme() {
//        val theme = themeUseCases.getThemeUseCase.invoke()
//        val themeMode = themeModeUseCases.getThemeModeUseCase.invoke()
//
//        when (theme) {
//            Themes.HellStyle.name -> {
//                when(themeMode) {
//                    ThemeModes.FollowSystem.name -> {
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//                        setTheme(R.style.Theme_HellWeather)
//                    }
//
//                    ThemeModes.Dark.name -> {
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                        setTheme(R.style.Theme_HellWeather)
//                    }
//
//                    ThemeModes.Light.name -> {
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                        setTheme(R.style.Theme_HellWeather)
//                    }
//                }
//            }
//        }
//    }

//    private fun setupMaterialShapeToDrawer() {
//        val navViewBackground:MaterialShapeDrawable = binding.navigationView.background as MaterialShapeDrawable
//        navViewBackground.shapeAppearanceModel = navViewBackground.shapeAppearanceModel
//            .toBuilder()
//            .setTopRightCorner(CornerFamily.ROUNDED, resources.getDimensionPixelSize(R.dimen.drawer_corner).toFloat())
//            .setBottomRightCorner(CornerFamily.ROUNDED, resources.getDimensionPixelSize(R.dimen.drawer_corner).toFloat())
//            .build()
//
//    }

//    private fun setObservers() {
//        viewModel.getUserLocationsList().observe(this) {
//            checkService()
//            if (!viewModel.isInProgress()) {
//                if (viewModel.getStatus().value != State.Empty) {
//                    viewModel.fetchWeather(it)
//                }
//            }
//        }
//    }
//
//    fun onRefresh() {
//        val locationList = viewModel.getUserLocationsList().value
//
//        if (locationList.isNullOrEmpty())
//            return
//
//        viewModel.fetchWeather(locationList)
//    }
//
//    private fun checkService() = CoroutineScope(Dispatchers.IO).launch {
//        val serviceMode = serviceUseCases.getServiceModeUseCase.invoke()
//
//        if (serviceMode && !WeatherService.isRunning()) {
//            serviceControl(Selector.Enable)
//        }
//    }
//
//    fun serviceControl(action: Enum<Selector>) = CoroutineScope(Dispatchers.IO).launch {
//        when (action) {
//            Selector.Enable -> {
//                if (!WeatherService.isRunning()) {
//                    startService(viewModel.getUserLocationsList().value)
//                }
//            }
//            Selector.Disable -> {
//                if (WeatherService.isRunning()) {
//                    WeatherService.stopService(this@MainActivity)
//                }
//            }
//            Selector.Reboot -> {
//                if (WeatherService.isRunning()) {
//                    WeatherService.stopService(this@MainActivity)
//                    startService(viewModel.getUserLocationsList().value)
//                }
//            }
//        }
//    }
//
//    private suspend fun startService(locationList: List<UserLocationParam>?) {
//        if (!locationList.isNullOrEmpty()) {
//            val locationStr = serviceUseCases.getServiceLocationUseCase.invoke()
//
//            var userLocationPos = 0
//
//            for (n in locationList.indices) {
//                if (locationStr == locationList[n].locationName)
//                    userLocationPos = n
//            }
//
//            WeatherService.startService(this@MainActivity, locationList[userLocationPos])
//        }
//    }
//
//    override fun onBackPressed() {
//        if (binding.drawerLayout.isOpen)
//        {
//            closeDrawer()
//        }
//        else
//        {
//            super.onBackPressed()
//        }
//    }
//
//    fun openDrawer() = binding.drawerLayout.open()
//
//    private fun closeDrawer() = binding.drawerLayout.close()
//
//    private fun isOnHomeFragment(): Boolean = navController.currentDestination?.id == R.id.homeFragment

//}

