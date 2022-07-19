package com.hellguy39.hellweather.presentation.fragments.settings

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialFadeThrough
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentSettingsBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings),
    Toolbar.OnMenuItemClickListener {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        binding.run {
            toolbar.setNavigationOnClickListener { (activity as MainActivity).openDrawer() }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            else -> false
        }
    }

}