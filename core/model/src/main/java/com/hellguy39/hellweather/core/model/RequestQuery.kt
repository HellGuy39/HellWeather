package com.hellguy39.hellweather.core.model

sealed class RequestQuery {

    data class Coordinates(val lat: Double, val lon: Double): RequestQuery()

    data class City(val name: String): RequestQuery()

    data object AutoIP: RequestQuery()

    fun asValue(): String {
        return when(this) {
            is Coordinates -> "${this.lat},${this.lon}"
            is City -> this.name
            is AutoIP -> "auto:ip"
        }
    }

}
