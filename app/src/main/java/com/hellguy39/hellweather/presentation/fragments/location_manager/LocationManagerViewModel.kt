package com.hellguy39.hellweather.presentation.fragments.location_manager

import androidx.lifecycle.ViewModel
import com.hellguy39.hellweather.repository.database.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationManagerViewModel @Inject constructor(
    private val repository: LocationRepository
): ViewModel() {

    val locations = repository.getLocations()

    fun onEvent(event: LocationListEvent) {
        when(event) {
            is LocationListEvent.OnDeleteLocation -> {

            }
            is LocationListEvent.OnAddItemClick -> {

            }
            is LocationListEvent.OnDoneChange -> {

            }
            is LocationListEvent.OnItemClick -> {

            }
            is LocationListEvent.OnUndoDeleteClick -> {

            }
        }
    }

}