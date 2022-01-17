package com.hellguy39.hellweather.presentation.fragments.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hellguy39.hellweather.BuildConfig
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentAppInfoBinding
import com.hellguy39.hellweather.databinding.FragmentHomeBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.utils.DISABLE

class AppInfoFragment : Fragment() {

    private lateinit var binding: FragmentAppInfoBinding
    private val hazeTittle = "Haze"
    private val hazeText = "“Change of weather is the discourse of fools.”\n" +
            "— Thomas Fuller"
    private val postText = "All data parsed from OpenWeather"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).updateToolbarMenu(DISABLE)
        (activity as MainActivity).setToolbarTittle(getString(R.string.tittle_info))

        return inflater.inflate(R.layout.fragment_app_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAppInfoBinding.bind(view)

        binding.tvUpdateTittle.text = hazeTittle
        binding.tvUpdateText.text = hazeText
        binding.tvPostText.text = postText
        binding.tvCodename.text = "Codename: ${hazeTittle}"
        binding.tvVersion.text = "Version: ${BuildConfig.VERSION_NAME}"

    }
}