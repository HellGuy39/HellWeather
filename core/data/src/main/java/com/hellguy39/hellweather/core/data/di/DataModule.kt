package com.hellguy39.hellweather.core.data.di

import com.hellguy39.hellweather.core.data.repository.WeatherRepositoryImpl
import com.hellguy39.hellweather.core.domain.repository.WeatherRepository
import org.koin.dsl.module

val dataModule = module {

    single<WeatherRepository> {
        WeatherRepositoryImpl(
            networkDataSource = get()
        )
    }

}