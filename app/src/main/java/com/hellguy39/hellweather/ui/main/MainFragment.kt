package com.hellguy39.hellweather.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.MainFragmentBinding
import com.hellguy39.hellweather.utils.observe

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainFragmentBinding.bind(view)

        binding.rootView.setColorSchemeResources(R.color.purple_700)
        binding.rootView.setOnRefreshListener { viewModel.onRefresh() }

        observe(viewModel.loading) {
            binding.rootView.isRefreshing = it
        }

        observe(viewModel.error) {
            Snackbar.make(view, "Error", Snackbar.LENGTH_SHORT).show()
        }

    }

}