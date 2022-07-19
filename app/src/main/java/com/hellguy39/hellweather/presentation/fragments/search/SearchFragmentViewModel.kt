package com.hellguy39.hellweather.presentation.fragments.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.usecase.GetLocationInfoListUseCase
import com.hellguy39.hellweather.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(
    private val getLocationInfoListUseCase: GetLocationInfoListUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(SearchFragmentState())
    val uiState = _uiState

    fun fetchLocations(cityName: String) = viewModelScope.launch {
        getLocationInfoListUseCase.invoke(cityName).collect { resource ->
            when(resource) {
                is Resource.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            data = resource.data
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { state ->
                        state.copy(
                            error = resource.message
                        )
                    }
                }
                is Resource.Loading -> {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = resource.isLoading
                        )
                    }
                }
                else -> Unit
            }
        }
    }

}