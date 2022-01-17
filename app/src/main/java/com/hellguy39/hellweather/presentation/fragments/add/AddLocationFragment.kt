package com.hellguy39.hellweather.presentation.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentAddLocationBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import com.hellguy39.hellweather.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddLocationFragment : Fragment(R.layout.fragment_add_location) {

    private lateinit var binding: FragmentAddLocationBinding
    private lateinit var fragView: View
    private lateinit var viewModel: AddLocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, AddLocationViewModelFactory(requireContext()))[AddLocationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).setToolbarTittle(getString(R.string.tittle_location_manager))
        (activity as MainActivity).updateToolbarMenu(DISABLE)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        fragView = view
        binding = FragmentAddLocationBinding.bind(view)

        viewModel.userLocationLive.observe(this, {
            //saveCord(it)
        })

        viewModel.requestResLive.observe(this, {
            loadController(DISABLE)
            when(it) {
                SUCCESSFUL -> {
                    navigate(viewModel.userLocationLive.value!!)
                    viewModel.clearData() //strange solution
                }
                EMPTY_BODY -> {
                    fragView.shortSnackBar("City not found")
                }
                ERROR -> {
                    fragView.shortSnackBar("City not found")
                }
                FAILURE -> {
                    fragView.shortSnackBar("Server not responding")
                }
            }
        })

        viewModel.isLoadingLive.observe(this, {
            if (it) {
                loadController(ENABLE)
            }
            else
            {
                loadController(DISABLE)
            }
        })

        binding.fabNext.setOnClickListener {

            val input = binding.etCity.text.toString()

            if (checkTextField(input))
            {
                loadController(ENABLE)
                CoroutineScope(Dispatchers.Default).launch {
                    viewModel.checkCityInAPI(input)
                }
            }
            else
            {
                fragView.shortSnackBar("Not funny")
            }

        }
    }



    private fun loadController(action: String) {
            when (action) {
                ENABLE -> {
                    binding.etCity.isEnabled = false
                    binding.fabNext.isEnabled = false
                    binding.progressLinear.visibility = View.VISIBLE
                }
                DISABLE -> {
                    binding.etCity.isEnabled = true
                    binding.fabNext.isEnabled = true
                    binding.progressLinear.visibility = View.INVISIBLE
                }
            }
    }

    private fun checkTextField(input: String) : Boolean = !TextUtils.isEmpty(input)


    private fun navigate(userLocation: UserLocation) = fragView.findNavController()
        .navigate(AddLocationFragmentDirections.actionAddCityFragmentToConfirmationCityFragment(userLocation))
}