package com.hellguy39.hellweather.presentation.fragments.page

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentNoInternetPageBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.utils.setToolbarNavigation

private const val ERROR_MESSAGE_ARG = "em_arg"

class ErrorPageFragment : Fragment(R.layout.fragment_no_internet_page) {

    private lateinit var _binding: FragmentNoInternetPageBinding

    private lateinit var errorMessage: String

    companion object {
        @JvmStatic
        fun newInstance(errorMessage: String) = ErrorPageFragment().apply {
            arguments = Bundle().apply {
                putString(ERROR_MESSAGE_ARG, errorMessage)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            errorMessage = it.getString(ERROR_MESSAGE_ARG) as String
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentNoInternetPageBinding.bind(view)
        _binding.toolbar.setToolbarNavigation(toolbar = _binding.toolbar, activity = activity as MainActivity)
        _binding.toolbar.title = errorMessage

        _binding.btnRetry.setOnClickListener {
            (activity as MainActivity).onRefresh()
        }
    }

}