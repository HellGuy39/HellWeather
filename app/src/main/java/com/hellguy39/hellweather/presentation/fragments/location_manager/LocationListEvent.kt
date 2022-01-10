package com.hellguy39.hellweather.presentation.fragments.location_manager

import com.hellguy39.hellweather.repository.database.pojo.UserLocation

sealed class LocationListEvent {
    data class OnDeleteLocation(val location: UserLocation) : LocationListEvent()
    data class OnDoneChange(val location: UserLocation, val isDone: Boolean) : LocationListEvent()
    object OnUndoDeleteClick: LocationListEvent()
    data class OnItemClick(val location: UserLocation): LocationListEvent()
    object OnAddItemClick: LocationListEvent()
}
