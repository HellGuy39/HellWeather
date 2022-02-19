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
import com.hellguy39.hellweather.presentation.activities.main.MainActivityViewModel
import com.hellguy39.hellweather.data.enteties.UserLocation
import com.hellguy39.hellweather.utils.DISABLE
import com.hellguy39.hellweather.utils.ENABLE
import com.hellguy39.hellweather.utils.NONE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForegroundServiceFragment : Fragment(R.layout.foreground_service_fragment) {

    /*companion object {
        fun newInstance() = ForegroundServiceFragment()
    }*/

    private val updTimeItemsStr = listOf("15 mins", "30 mins", "1 hour", "3 hours", "6 hours")
    private val updTimeItemsInt = listOf(15, 30, 60, 60 * 3, 60 * 6)

    private lateinit var _viewModel: ForegroundServiceViewModel
    private lateinit var _mainViewModel: MainActivityViewModel
    private lateinit var _binding: ForegroundServiceFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel = ViewModelProvider(this)[ForegroundServiceViewModel::class.java]
        _mainViewModel = ViewModelProvider(activity as MainActivity)[MainActivityViewModel::class.java]
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

    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).launch {
            val userLocationList = _mainViewModel.userLocationsLive.value

            if (userLocationList != null)
                setupLocation(userLocationList)
            else
                _binding.acLocation.isEnabled = false

            setupUpdTime()
            setupServiceSwitch()
            setupServiceSwitch()
        }
    }

    private fun setupServiceSwitch() {

        val isEnabled = _viewModel.getServiceMode()

        if (isEnabled)
            _binding.serviceSwitch.isChecked = true

        _binding.serviceSwitch.setOnCheckedChangeListener { compoundButton, b ->
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

    private fun setupLocation(list: List<UserLocation>) {
        val locationNameList = mutableListOf<String>()

        if (list.isEmpty())
            return

        val selectedLoc = _viewModel.getServiceLocation()
        var selectedListPos = 0

        for (n in list.indices) {

            if (selectedLoc != NONE && list[n].locationName == selectedLoc)
                selectedListPos = n

            locationNameList.add(list[n].locationName)
        }

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, locationNameList)
        (_binding.acLocation as? AutoCompleteTextView)?.setAdapter(adapter)

        if (_binding.acLocation.adapter.getItem(selectedListPos) != null)
            _binding.acLocation.setText(_binding.acLocation.adapter.getItem(selectedListPos).toString(), false)

        _binding.acLocation.setOnItemClickListener { adapterView, view, i, l ->
            _viewModel.saveServiceLocation(_binding.acLocation.adapter.getItem(i).toString())
        }

    }

    private fun setupUpdTime() {

        val selectedTime = _viewModel.getUpdateTime()
        var selectedPos = 3 // 3 hour

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, updTimeItemsStr)
        (_binding.acUpdTime as? AutoCompleteTextView)?.setAdapter(adapter)

        for (n in updTimeItemsInt.indices) {

            if (selectedTime == updTimeItemsInt[n])
                selectedPos = n

        }

        _binding.acUpdTime.setText(_binding.acUpdTime.adapter.getItem(selectedPos).toString(), false)

        _binding.acUpdTime.setOnItemClickListener { adapterView, view, i, l ->
            _viewModel.saveUpdateTime(updTimeItemsInt[i])
        }

    }

}