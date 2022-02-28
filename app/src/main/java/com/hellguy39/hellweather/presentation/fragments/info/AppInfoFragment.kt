package com.hellguy39.hellweather.presentation.fragments.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hellguy39.hellweather.BuildConfig
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentAppInfoBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.utils.Selector
import com.hellguy39.hellweather.utils.setToolbarNavigation

class AppInfoFragment : Fragment() {

    private lateinit var _binding: FragmentAppInfoBinding
    private val hazeTittle = "Haze"
    private val hazeText = "“Change of weather is the discourse of fools.”\n" +
            "— Thomas Fuller"
    private val postText = "All data parsed from OpenWeather"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAppInfoBinding.bind(view)

        _binding.toolbar.setToolbarNavigation(toolbar = _binding.toolbar, activity = activity as MainActivity)

        _binding.tvUpdateTittle.text = hazeTittle
        _binding.tvUpdateText.text = hazeText
        _binding.tvPostText.text = postText
        _binding.tvCodename.text = String.format(resources.getString(R.string.codename), hazeTittle)
        _binding.tvVersion.text =  String.format(resources.getString(R.string.version), BuildConfig.VERSION_NAME)

    }
}