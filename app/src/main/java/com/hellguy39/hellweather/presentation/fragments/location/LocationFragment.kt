package com.hellguy39.hellweather.presentation.fragments.location

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentLocationBinding

class LocationFragment : Fragment(R.layout.fragment_location) {

    private lateinit var binding: FragmentLocationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLocationBinding.bind(view)
    }

}