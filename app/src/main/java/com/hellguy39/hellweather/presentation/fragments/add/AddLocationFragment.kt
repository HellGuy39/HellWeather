package com.hellguy39.hellweather.presentation.fragments.add

import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentAddLocationBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import com.hellguy39.hellweather.utils.*


class AddLocationFragment : Fragment() {

    private lateinit var binding: FragmentAddLocationBinding
    private lateinit var fragView: View
    private lateinit var viewModel: AddLocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, AddLocationViewModelFactory(requireContext()))[AddLocationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_add_location, container, false)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        fragView = view
        binding = FragmentAddLocationBinding.bind(view)

        viewModel.userLocationLive.observe(this, {
            saveCord(it)
        })

        viewModel.requestResLive.observe(this, {
            when(it) {
                SUCCESSFUL -> {
                    disableFirstBoot()
                    (activity as MainActivity).drawerControl(ENABLE)
                    navigate()
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

        binding.btnCnt.setOnClickListener {

            val input = binding.etCity.text.toString()

            if (checkTextField(input))
            {
                viewModel.checkCityInAPI(input)
            }
            else
            {
                fragView.shortSnackBar("Not funny")
            }

        }
    }
    //I know that it is wrong
    private fun saveCord(usrLoc: UserLocation) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val edit: SharedPreferences.Editor = sharedPreferences.edit()
        edit.putString("lat", usrLoc.lat)
        edit.putString("lon", usrLoc.lon)
        edit.putString("cityName", usrLoc.cityName)
        edit.apply()
    }
    private fun disableFirstBoot() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val edit: SharedPreferences.Editor = sharedPreferences.edit()
        edit.putBoolean("first_boot", false)
        edit.apply()
    }

    private fun loadController(action: String) {
            when (action) {
                ENABLE -> {
                    binding.etCity.isEnabled = false
                    binding.btnCnt.isEnabled = false
                    binding.progressLinear.visibility = View.VISIBLE
                    binding.tvWait.visibility = View.VISIBLE
                }
                DISABLE -> {
                    binding.etCity.isEnabled = true
                    binding.btnCnt.isEnabled = true
                    binding.progressLinear.visibility = View.INVISIBLE
                    binding.tvWait.visibility = View.INVISIBLE
                }
            }
    }

    private fun checkTextField(input: String) : Boolean {
        return !TextUtils.isEmpty(input) && input.length > 3
    }

    private fun navigate() = fragView.findNavController().navigate(AddLocationFragmentDirections.actionAddCityFragmentToHomeFragment())

}