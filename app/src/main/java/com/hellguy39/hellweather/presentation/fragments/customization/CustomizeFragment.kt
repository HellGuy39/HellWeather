package com.hellguy39.hellweather.presentation.fragments.customization

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.CustomizeFragmentBinding

class CustomizeFragment : Fragment(R.layout.customize_fragment) {

    private lateinit var viewModel: CustomizeViewModel
    private lateinit var binding: CustomizeFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CustomizeViewModel::class.java]
        binding = CustomizeFragmentBinding.bind(view)
        binding.fabBack.setOnClickListener {
            view.findNavController().popBackStack()
        }
    }

}