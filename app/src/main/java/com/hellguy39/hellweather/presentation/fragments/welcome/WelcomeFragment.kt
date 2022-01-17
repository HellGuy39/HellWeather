package com.hellguy39.hellweather.presentation.fragments.welcome

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentWelcomeBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.utils.DISABLE

class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).drawerControl(DISABLE)
        (activity as MainActivity).setToolbarTittle(getString(R.string.tittle_welcome))
        (activity as MainActivity).updateToolbarMenu(DISABLE)

        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWelcomeBinding.bind(view)

        binding.fabNext.setOnClickListener {
            view.findNavController().navigate(R.id.action_welcomeFragment_to_addCityFragment)
        }

    }

}