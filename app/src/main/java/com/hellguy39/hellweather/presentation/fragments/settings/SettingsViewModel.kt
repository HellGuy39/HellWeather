package com.hellguy39.hellweather.presentation.fragments.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.usecase.prefs.theme.ThemeUseCases
import com.hellguy39.hellweather.domain.usecase.prefs.theme_mode.ThemeModeUseCases
import com.hellguy39.hellweather.domain.usecase.prefs.units.UnitsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val unitsUseCases: UnitsUseCases,
    private val themeModeUseCase: ThemeModeUseCases,
    private val themeUseCases: ThemeUseCases
) : ViewModel() {

    private val unitsLiveData = MutableLiveData<String>()
    private val themeModeLiveData = MutableLiveData<String>()
    private val themeLiveData = MutableLiveData<String>()

    init {
        fetchOptions()
    }

    private fun fetchOptions() {
        viewModelScope.launch(Dispatchers.IO) {
            val units = unitsUseCases.getUnitsUseCase.invoke()
            val theme = themeUseCases.getThemeUseCase.invoke()
            val themeMode = themeModeUseCase.getThemeModeUseCase.invoke()

            updateUnitsLiveData(units)
            updateThemeLiveData(theme)
            updateThemeModeLiveData(themeMode)
        }
    }

    private fun updateUnitsLiveData(units: String) = viewModelScope.launch {
        unitsLiveData.value = units
    }

    private fun updateThemeLiveData(theme: String) = viewModelScope.launch {
        themeLiveData.value = theme
    }

    private fun updateThemeModeLiveData(mode: String) = viewModelScope.launch {
        themeModeLiveData.value = mode
    }

    fun saveUnits(units: String) = viewModelScope.launch(Dispatchers.IO) {
        unitsUseCases.saveUnitsUseCase.invoke(units = units)
        updateUnitsLiveData(units)
    }

    fun saveThemeMode(mode: String) = viewModelScope.launch(Dispatchers.IO) {
        themeModeUseCase.saveThemeModeUseCase.invoke(mode = mode)
        updateThemeLiveData(mode)
    }

    fun saveTheme(theme: String) = viewModelScope.launch(Dispatchers.IO) {
        themeUseCases.saveThemeUseCase.invoke(theme = theme)
        updateThemeLiveData(theme)
    }

    fun getUnits(): LiveData<String> {
        return unitsLiveData
    }

    fun getThemeMode(): LiveData<String> {
        return themeModeLiveData
    }

    fun getTheme(): LiveData<String> {
        return themeLiveData
    }
}