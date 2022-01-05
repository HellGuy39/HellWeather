package com.hellguy39.hellweather.presentation.fragments.confirmation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hellguy39.hellweather.R

class ConfirmationCityFragment : Fragment(R.layout.confirmation_city_fragment) {

    private lateinit var viewModel: ConfirmationCityViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ConfirmationCityViewModel::class.java]
    }

}