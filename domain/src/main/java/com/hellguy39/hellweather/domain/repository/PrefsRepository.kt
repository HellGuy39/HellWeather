package com.hellguy39.hellweather.domain.repository

interface PrefsRepository {
    fun getUnits() : String
    suspend fun saveUnits(units: String)

    fun getFirstBootValue() : Boolean
    suspend fun saveFirstBootValue(value: Boolean)

    fun getServiceMode(): Boolean
    suspend fun saveServiceMode(value: Boolean)

    fun getServiceLocationUseCase(): String
    suspend fun saveServiceLocationUseCase(locationName: String)

    fun getServiceUpdateTime(): Int
    suspend fun saveServiceUpdateTime(minutes: Int)
}