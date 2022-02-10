package com.hellguy39.hellweather.presentation.fragments.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.SearchFragmentBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.utils.DISABLE

class SearchFragment : Fragment(R.layout.search_fragment) {

    private lateinit var _viewModel: SearchViewModel
    private lateinit var _binding: SearchFragmentBinding
    private lateinit var _fragView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel = ViewModelProvider(this)[SearchViewModel::class.java]

    }

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
        _fragView = view
        _binding = SearchFragmentBinding.bind(view)

        _binding.ibSearch.setOnClickListener {
            refreshing(true)
        }
    }

    private fun refreshing(b: Boolean) {
        if (b)
        {
            _binding.tfCity.isEnabled = false
            _binding.progressIndicator.visibility = View.VISIBLE
            _binding.ibSearch.isEnabled = false
            (activity as MainActivity).setToolbarTittle("Searching...")
        }
        else
        {
            _binding.tfCity.isEnabled = true
            _binding.progressIndicator.visibility = View.INVISIBLE
            _binding.ibSearch.isEnabled = true
        }
    }

    private fun checkInput(s : String) : Boolean = TextUtils.isEmpty(s)
}