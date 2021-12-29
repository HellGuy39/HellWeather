package com.hellguy39.hellweather

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.hellguy39.hellweather.databinding.FragmentAddCityBinding


class AddCityFragment : Fragment() {

    private lateinit var binding: FragmentAddCityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddCityBinding.bind(view)

        binding.btnCnt.setOnClickListener {
            //view.findNavController().navigate(R.id.action_addCityFragment_to_homeFragment)
            val input = binding.etCity.text.toString()
            if (checkTextField(input))
            {
                if (checkCityInAPI(input))
                {

                }
                else
                {

                }
            }
            else
            {

            }
        }
    }

    private fun checkTextField(input: String) : Boolean {
        return !TextUtils.isEmpty(input) && input.length > 3
    }

    private fun checkCityInAPI(input: String) : Boolean {
        return true
    }
}