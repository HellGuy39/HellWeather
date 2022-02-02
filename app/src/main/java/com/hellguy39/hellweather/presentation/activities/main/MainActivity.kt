package com.hellguy39.hellweather.presentation.activities.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.MainActivityBinding
import com.hellguy39.hellweather.presentation.fragments.home.HomeFragment
import com.hellguy39.hellweather.presentation.fragments.home.HomeFragmentDirections
import com.hellguy39.hellweather.utils.DISABLE
import com.hellguy39.hellweather.utils.ENABLE
import com.hellguy39.hellweather.utils.SUCCESSFUL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener, MenuItem.OnMenuItemClickListener {

    //private val locationManagerViewModel : LocationManagerViewModel by viewModels()
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: MainActivityBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private  var toolBarMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        toolBarMenu = binding.topAppBar.menu

        val firstBoot: Boolean = intent.getBooleanExtra("first_boot", false)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.topAppBar.setNavigationOnClickListener {
            openDrawer()
        }

        binding.topAppBar.menu.getItem(0).setOnMenuItemClickListener(this)

        val btnInfo = binding.navigationView.getHeaderView(0).findViewById<Button>(R.id.btnInfo)
        val btnSettings = binding.navigationView.getHeaderView(0).findViewById<Button>(R.id.btnSettings)

        btnInfo.setOnClickListener(this)
        btnSettings.setOnClickListener(this)

        binding.navigationView.setCheckedItem(R.id.homeFragment)
        NavigationUI.setupWithNavController(binding.navigationView, navController)

        /*setSupportActionBar(binding.topAppBar)
        NavigationUI.setupActionBarWithNavController(this, navController)*/

        val navViewBackground:MaterialShapeDrawable = binding.navigationView.background as MaterialShapeDrawable
        navViewBackground.shapeAppearanceModel = navViewBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(CornerFamily.ROUNDED, resources.getDimensionPixelSize(R.dimen.drawer_corner).toFloat())
            .setBottomRightCorner(CornerFamily.ROUNDED, resources.getDimensionPixelSize(R.dimen.drawer_corner).toFloat())
            .build()

        if (firstBoot)
        {
            drawerControl(DISABLE)

            if(navController.currentDestination?.id == R.id.homeFragment)
            {
                navController.navigate(HomeFragmentDirections.actionHomeFragmentToWelcomeFragment())
            }
        }

        viewModel.userLocationsLive.observe(this) {
            viewModel.loadAllLocation(it)
        }

        viewModel.statusLive.observe(this) {
            if (it == SUCCESSFUL) {

            }
        }

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnInfo -> {
                navController.navigate(R.id.appInfoFragment)
                closeDrawer()
            }
            R.id.btnSettings -> {
                navController.navigate(R.id.settingsFragment)
                closeDrawer()
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
                    Log.i("TAG", "Drawer disabled")
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
                /*if (navController.currentDestination?.id == R.id.homeFragment) {
                    (navHostFragment.childFragmentManager.fragments[0] as HomeFragment).onRefresh()
                }*/
            }
        }
        return true
    }


}

