package com.hellguy39.hellweather.presentation.fragments.customization

import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.card.MaterialCardView
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.CustomizeFragmentBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.utils.DISABLE

class CustomizeFragment : Fragment(R.layout.customize_fragment) {

    private lateinit var viewModel: CustomizeViewModel
    private lateinit var binding: CustomizeFragmentBinding
    private val themes = listOf("Default", "Sky inspired")
    private val themeMode = listOf("Follow system", "Light", "Dark")
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).updateToolbarMenu(DISABLE)
        (activity as MainActivity).setToolbarTittle(getString(R.string.tittle_customize))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CustomizeViewModel::class.java]
        binding = CustomizeFragmentBinding.bind(view)
        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        binding.sliderBlur.addOnChangeListener { slider, value, fromUser ->
            val cardBlur : MaterialCardView = binding.cardBlur
            when(value) {
                00.0F -> { cardBlur.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.whiteBlur00, null) }
                10.0F -> { cardBlur.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.whiteBlur10, null) }
                20.0F -> { cardBlur.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.whiteBlur20, null) }
                30.0F -> { cardBlur.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.whiteBlur30, null) }
                40.0F -> { cardBlur.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.whiteBlur40, null) }
                50.0F -> { cardBlur.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.whiteBlur50, null) }
                60.0F -> { cardBlur.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.whiteBlur60, null) }
                70.0F -> { cardBlur.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.whiteBlur70, null) }
                80.0F -> { cardBlur.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.whiteBlur80, null) }
                90.0F -> { cardBlur.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.whiteBlur90, null) }
                100.0F -> { cardBlur.backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.whiteBlur100, null) }
            }
        }

        binding.sliderCornerRad.addOnChangeListener { slider, value, fromUser ->
            val radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                context?.resources?.displayMetrics)
            binding.cardBlur.radius = radius
        }

        val adapterTheme = ArrayAdapter(requireContext(), R.layout.list_item, themes)
        val adapterMode = ArrayAdapter(requireContext(), R.layout.list_item, themeMode)

        binding.acTheme.setAdapter(adapterTheme)
        binding.acMode.setAdapter(adapterMode)

        binding.acTheme.setText(binding.acTheme.adapter.getItem(0).toString(), false)
        binding.acMode.setText(binding.acMode.adapter.getItem(0).toString(), false)

        binding.tfMode.isEnabled = false
        binding.tfTheme.isEnabled = false

    }

    override fun onStart() {
        super.onStart()
        binding.sliderBlur.isEnabled = false
        binding.sliderCornerRad.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //saveData()
    }

    private fun changeUi() {

    }

    private fun saveData(theme: String, tMode: String, blur: Int, corRad: Int) {
        sharedPreferences.edit().apply {
            putString("theme", theme)
            putString("tMode", tMode)
            putInt("blur", blur)
            putInt("cordRad", corRad)
        }.apply()
    }
}