package com.hellguy39.hellweather.di

import com.google.android.gms.location.LocationServices
import com.hellguy39.hellweather.activity.WeatherViewModel
import com.hellguy39.hellweather.service.ILocationClient
import com.hellguy39.hellweather.service.LocationClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        WeatherViewModel(
            weatherRepository = get()
        )
    }

    single<ILocationClient> {
        LocationClient(
            context = get(),
            client = LocationServices.getFusedLocationProviderClient(get())
        )
    }

}