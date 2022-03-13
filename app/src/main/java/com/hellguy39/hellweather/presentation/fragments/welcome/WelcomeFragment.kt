package com.hellguy39.hellweather.presentation.fragments.welcome

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentWelcomeBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.utils.Selector

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWelcomeBinding.bind(view)

        binding.fabNext.setOnClickListener {
            view.findNavController().navigate(R.id.action_welcomeFragment_to_addCityFragment)
        }

    }

}