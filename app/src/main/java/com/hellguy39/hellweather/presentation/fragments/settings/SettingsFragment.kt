package com.hellguy39.hellweather.presentation.fragments.settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.SettingsFragmentBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity

class SettingsFragment : Fragment(R.layout.settings_fragment) {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: SettingsFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).setToolbarTittle("Settings")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SettingsFragmentBinding.bind(view)
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

    }

}