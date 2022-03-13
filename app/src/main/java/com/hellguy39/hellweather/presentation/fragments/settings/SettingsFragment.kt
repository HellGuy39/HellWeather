package com.hellguy39.hellweather.presentation.fragments.settings

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.SettingsFragmentBinding
import com.hellguy39.hellweather.domain.utils.ThemeModes
import com.hellguy39.hellweather.domain.utils.Themes
import com.hellguy39.hellweather.domain.utils.Unit
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.settings_fragment) {

    private lateinit var _viewModel: SettingsViewModel
    private lateinit var _binding: SettingsFragmentBinding

    private val languageList = listOf("System","Russian", "English", "Deutsch", "Francais")
    private val unitsList = listOf(Unit.Standard.name, Unit.Metric.name, Unit.Imperial.name)
    private val themeList = listOf(Themes.HellStyle.name)
    private val modeList = listOf(ThemeModes.FollowSystem.name, ThemeModes.Dark.name, ThemeModes.Light.name)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = SettingsFragmentBinding.bind(view)

        _binding.toolbar.setNavigationOnClickListener {
            (activity as MainActivity).openDrawer()
        }

        setObservers()
    }

    private fun setObservers() {
        _viewModel.getUnits().observe(viewLifecycleOwner) { units ->
            setupUnits(units = units)
        }
        _viewModel.getTheme().observe(viewLifecycleOwner) { theme ->
            setupThemes(theme = theme)
        }
        _viewModel.getThemeMode().observe(viewLifecycleOwner) { mode ->
            setupMode(mode = mode)
        }
    }

    private fun setupThemes(theme: String) {
        val adapterTheme = ArrayAdapter(requireContext(), R.layout.list_item, themeList)
        _binding.acTheme.setAdapter(adapterTheme)

        when (theme) {
            Themes.HellStyle.name -> {
                _binding.acTheme.setText(_binding.acTheme.adapter.getItem(0).toString(), false)
            }
        }

        _binding.acTheme.setOnItemClickListener { _, _, i, _ ->
            _viewModel.saveTheme(_binding.acTheme.adapter.getItem(i).toString())
        }

    }

    private fun setupLanguages() {
        val adapterLanguage = ArrayAdapter(requireContext(), R.layout.list_item, languageList)
        _binding.acLanguage.setAdapter(adapterLanguage)
        _binding.acLanguage.setText(_binding.acLanguage.adapter.getItem(0).toString(), false)

    }

    private fun setupMode(mode: String) {
        val adapterMode = ArrayAdapter(requireContext(), R.layout.list_item, modeList)
        _binding.acMode.setAdapter(adapterMode)

        when(mode) {
            ThemeModes.FollowSystem.name -> {
                _binding.acMode.setText(_binding.acMode.adapter.getItem(0).toString(), false)
            }
            ThemeModes.Dark.name -> {
                _binding.acMode.setText(_binding.acMode.adapter.getItem(1).toString(), false)
            }
            ThemeModes.Light.name -> {
                _binding.acMode.setText(_binding.acMode.adapter.getItem(2).toString(), false)
            }
        }

        _binding.acMode.setOnItemClickListener { _, _, i, _ ->
            _viewModel.saveThemeMode(_binding.acMode.adapter.getItem(i).toString())
        }
    }

    private fun setupUnits(units: String) {
        val adapterUnits = ArrayAdapter(requireContext(), R.layout.list_item, unitsList)
        _binding.acUnits.setAdapter(adapterUnits)

        when (units) {
            Unit.Standard.name -> {
                _binding.acUnits.setText(_binding.acUnits.adapter.getItem(0).toString(), false)
            }
            Unit.Metric.name -> {
                _binding.acUnits.setText(_binding.acUnits.adapter.getItem(1).toString(), false)
            }
            Unit.Imperial.name -> {
                _binding.acUnits.setText(_binding.acUnits.adapter.getItem(2).toString(), false)
            }
        }

        _binding.acUnits.setOnItemClickListener { _, _, i, _ ->
            _viewModel.saveUnits(_binding.acUnits.adapter.getItem(i).toString())
        }
    }
}