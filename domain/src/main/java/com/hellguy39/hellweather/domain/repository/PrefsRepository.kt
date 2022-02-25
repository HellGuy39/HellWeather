package com.hellguy39.hellweather.domain.repository

interface PrefsRepository {
    suspend fun getUnits() : String
    suspend fun saveUnits(units: String)
    suspend fun getFirstBootValue() : Boolean
    suspend fun saveFirstBootValue(value: Boolean)
}