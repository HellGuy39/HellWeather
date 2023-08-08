package com.hellguy39.hellweather.core.network.di

import com.hellguy39.hellweather.core.network.NetworkDataSource
import org.koin.dsl.module

val networkModule = module {
    single {
        NetworkDataSource()
    }
}