package com.hellguy39.hellweather.presentation.fragments.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentSearchBinding
import com.hellguy39.hellweather.domain.model.LocationInfo
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.adapter.LocationsInfoAdapter
import com.hellguy39.hellweather.utils.clearAndUpdateDataSet
import com.hellguy39.hellweather.utils.onSubmit
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search),
    View.OnClickListener, LocationsInfoAdapter.LocationInfoCallback {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchFragmentViewModel

    private val searchLocationInfoList = mutableListOf<LocationInfo>()
    private val recentLocationInfoList = mutableListOf<LocationInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
        viewModel = ViewModelProvider(this)[SearchFragmentViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        binding.run {
            btnMenu.setOnClickListener(this@SearchFragment)
            btnVoiceSearch.setOnClickListener(this@SearchFragment)
            btnSubmitSearch.setOnClickListener(this@SearchFragment)
            etSearch.onSubmit { submitSearch(etSearch.text.toString()) }
        }

        setupRecyclers()
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->
                updateUI(state)
            }
        }
    }

    private fun setProgressIndicatorEnabled(enable: Boolean) {
        if (enable)
            binding.progressIndicator.visibility = View.VISIBLE
        else
            binding.progressIndicator.visibility = View.GONE
    }

    private fun submitSearch(query: String) {
        if (query.isNotBlank() || query.isNotEmpty()) {
            viewModel.fetchLocations(query)
        } else {
            Snackbar.make(binding.root, "Text field is empty", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclers() {
        binding.run {
            rvRecently.apply {
                adapter = LocationsInfoAdapter(
                    dataSet = recentLocationInfoList,
                    callback = this@SearchFragment,
                    type = LocationsInfoAdapter.SearchType.Recent
                )
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            rvSearchResult.apply {
                adapter = LocationsInfoAdapter(
                    dataSet = searchLocationInfoList,
                    callback = this@SearchFragment,
                    type = LocationsInfoAdapter.SearchType.Search
                )
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    private fun updateUI(state: SearchFragmentState) {
        setProgressIndicatorEnabled(state.isLoading)
        state.data?.let { binding.rvSearchResult.clearAndUpdateDataSet(searchLocationInfoList, it) }
        state.cachedData?.let { binding.rvRecently.clearAndUpdateDataSet(recentLocationInfoList, it) }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            binding.btnMenu.id -> {
                (activity as MainActivity).openDrawer()
            }
            binding.btnVoiceSearch.id -> {

            }
            binding.btnSubmitSearch.id -> {
                submitSearch(binding.etSearch.text.toString())
            }
        }
    }

    override fun onClick(locationInfo: LocationInfo) {

    }
}