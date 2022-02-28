package com.hellguy39.hellweather.presentation.fragments.foreground_service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.ForegroundServiceFragmentBinding
import com.hellguy39.hellweather.domain.models.param.UserLocationParam
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.activities.main.MainActivityViewModel
import com.hellguy39.hellweather.utils.Selector
import com.hellguy39.hellweather.utils.setToolbarNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private var selectedLoc: String? = null
    private var selectedTime: Int = 3 * 60
    private var serviceMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel = ViewModelProvider(this)[ForegroundServiceViewModel::class.java]
        _mainViewModel = ViewModelProvider(activity as MainActivity)[MainActivityViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ForegroundServiceFragmentBinding.bind(view)
        _binding.toolbar.setToolbarNavigation(toolbar = _binding.toolbar, activity = activity as MainActivity)
    }

    override fun onStart() {
        super.onStart()

        //val userLocationList = _mainViewModel.userLocationsLive.value

        CoroutineScope(Dispatchers.IO).launch {

            selectedLoc = _viewModel.getServiceLocation()
            selectedTime = _viewModel.getUpdateTime()
            serviceMode = _viewModel.getServiceMode()

            withContext(Dispatchers.Main) {
                /*if (userLocationList != null)
                    setupLocation(userLocationList)
                else
                    _binding.acLocation.isEnabled = false
*/
                setupUpdTime()
                setupServiceSwitch()
                setupServiceSwitch()
            }
        }
    }

    private fun setupServiceSwitch() {

        if (serviceMode)
            _binding.serviceSwitch.isChecked = true

        _binding.serviceSwitch.setOnCheckedChangeListener { _, b ->
            if (b) {
                (activity as MainActivity).serviceControl(Selector.Enable)
            }
            else
            {
                (activity as MainActivity).serviceControl(Selector.Disable)
            }
            _viewModel.saveServiceMode(b)
        }
    }

    private fun setupLocation(list: List<UserLocationParam>) {
        val locationNameList = mutableListOf<String>()

        if (list.isEmpty())
            return

        var selectedListPos = 0

        for (n in list.indices) {

            if (selectedLoc != null && list[n].locationName == selectedLoc)
                selectedListPos = n

            locationNameList.add(list[n].locationName)
        }

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, locationNameList)
        (_binding.acLocation as? AutoCompleteTextView)?.setAdapter(adapter)

        if (_binding.acLocation.adapter.getItem(selectedListPos) != null)
            _binding.acLocation.setText(_binding.acLocation.adapter.getItem(selectedListPos).toString(), false)

        _binding.acLocation.setOnItemClickListener { _, _, i, _ ->
            _viewModel.saveServiceLocation(_binding.acLocation.adapter.getItem(i).toString())
        }

    }

    private fun setupUpdTime() {

        var selectedPos = 3 // 3 hour

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, updTimeItemsStr)
        (_binding.acUpdTime as? AutoCompleteTextView)?.setAdapter(adapter)

        for (n in updTimeItemsInt.indices) {

            if (selectedTime == updTimeItemsInt[n])
                selectedPos = n

        }

        _binding.acUpdTime.setText(_binding.acUpdTime.adapter.getItem(selectedPos).toString(), false)

        _binding.acUpdTime.setOnItemClickListener { _, _, i, _ ->
            _viewModel.saveUpdateTime(updTimeItemsInt[i])
        }

    }

}