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

class AppInfoFragment : Fragment() {

    private lateinit var binding: FragmentAppInfoBinding
    private val hazeTittle = "Haze"
    private val hazeText = "A mysterious shadow moved along the haze. " +
            "No one knows who it is, where it came from and where it is going. " +
            "Hundreds of such shadows disappeared along with the haze, hundreds remained. " +
            "Who knows what will happen to this one?\n" +
            "\n" +
            "Legends never come from nowhere"
    private val postText = "All data parsed from OpenWeather"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).setToolbarTittle("Info")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_app_info, container, false)

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