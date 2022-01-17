package com.hellguy39.hellweather.presentation.fragments.quick_weather

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.SearchFragmentBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.utils.DISABLE

class SearchFragment : Fragment(R.layout.search_fragment) {

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: SearchFragmentBinding
    private lateinit var fragView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).setToolbarTittle(getString(R.string.tittle_search))
        (activity as MainActivity).updateToolbarMenu(DISABLE)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragView = view
        binding = SearchFragmentBinding.bind(view)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        binding.btnSearch.setOnClickListener {

        }
    }

    private fun search() {

    }

    private fun checkInput(s : String) : Boolean = TextUtils.isEmpty(s)
}