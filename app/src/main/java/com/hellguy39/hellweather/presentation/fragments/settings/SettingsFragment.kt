package com.hellguy39.hellweather.presentation.fragments.settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.SettingsFragmentBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment() : Fragment(R.layout.settings_fragment) {

    private lateinit var _viewModel: SettingsViewModel
    private lateinit var _binding: SettingsFragmentBinding

    private val languageList = listOf("System","Russian", "English", "Deutsch", "Francais")
    private val unitsList = listOf(STANDARD, METRIC, IMPERIAL)
    private val themeList = listOf("HellStyle")
    private val modeList = listOf("Auto")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).setToolbarTittle(getString(R.string.tittle_settings))
        (activity as MainActivity).updateToolbarMenu(DISABLE)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = SettingsFragmentBinding.bind(view)

    }

    override fun onStart() {
        super.onStart()

        setupThemes()
        setupMode()
        setupLanguages()
        setupUnits()
    }

    private fun setupThemes() {
        val adapterTheme = ArrayAdapter(requireContext(), R.layout.list_item, themeList)
        _binding.acTheme.setAdapter(adapterTheme)
        _binding.acTheme.setText(_binding.acTheme.adapter.getItem(0).toString(), false)

        _binding.acTheme.isEnabled = false
    }

    private fun setupLanguages() {
        val adapterLanguage = ArrayAdapter(requireContext(), R.layout.list_item, languageList)
        _binding.acLanguage.setAdapter(adapterLanguage)
        _binding.acLanguage.setText(_binding.acLanguage.adapter.getItem(0).toString(), false)

        _binding.acLanguage.isEnabled = false
    }

    private fun setupMode() {
        val adapterMode = ArrayAdapter(requireContext(), R.layout.list_item, modeList)
        _binding.acMode.setAdapter(adapterMode)
        _binding.acMode.setText(_binding.acMode.adapter.getItem(0).toString(), false)

        _binding.acMode.isEnabled = false
    }

    private fun setupUnits() {
        val adapterUnits = ArrayAdapter(requireContext(), R.layout.list_item, unitsList)
        _binding.acUnits.setAdapter(adapterUnits)

        val currentOption = _viewModel.getSavedUnits()

        if (currentOption == STANDARD) {
            _binding.acUnits.setText(_binding.acUnits.adapter.getItem(0).toString(), false)
        } else if (currentOption == METRIC) {
            _binding.acUnits.setText(_binding.acUnits.adapter.getItem(1).toString(), false)
        } else if (currentOption == IMPERIAL) {
            _binding.acUnits.setText(_binding.acUnits.adapter.getItem(2).toString(), false)
        }

        _binding.acUnits.setOnItemClickListener { adapterView, view, i, l ->
            //_binding.acUnits.setText(_binding.acUnits.adapter.getItem(i).toString(), false)
            _viewModel.saveUnits(_binding.acUnits.adapter.getItem(i).toString())
        }

    }

}