package com.hellguy39.hellweather.presentation.fragments.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.hellguy39.hellweather.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}