package com.hellguy39.hellweather.presentation.fragments.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
    }

}