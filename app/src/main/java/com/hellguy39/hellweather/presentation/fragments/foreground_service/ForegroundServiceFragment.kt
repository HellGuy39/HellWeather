package com.hellguy39.hellweather.presentation.fragments.foreground_service

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.ForegroundServiceFragmentBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.utils.DISABLE
import com.hellguy39.hellweather.utils.ENABLE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForegroundServiceFragment : Fragment(R.layout.foreground_service_fragment) {

    companion object {
        fun newInstance() = ForegroundServiceFragment()
    }

    val updTimeItems = listOf("15 min", "30 min", "1 hour", "3 hour")
    private lateinit var _viewModel: ForegroundServiceViewModel
    private lateinit var _binding: ForegroundServiceFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel = ViewModelProvider(this)[ForegroundServiceViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).setToolbarTittle(getString(R.string.tittle_foreground_service))
        (activity as MainActivity).updateToolbarMenu(DISABLE)

        return inflater.inflate(R.layout.foreground_service_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ForegroundServiceFragmentBinding.bind(view)

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, updTimeItems)
        (_binding.acUpdTime as? AutoCompleteTextView)?.setAdapter(adapter)
        _binding.acUpdTime.setText(_binding.acUpdTime.adapter.getItem(0).toString(), false)

        _binding.serviceSwith.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                (activity as MainActivity).serviceControl(ENABLE)
            }
            else
            {
                (activity as MainActivity).serviceControl(DISABLE)
            }
            _viewModel.saveServiceMode(b)
        }

    }
}