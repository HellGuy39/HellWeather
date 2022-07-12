package com.hellguy39.hellweather.di

import com.hellguy39.hellweather.data.json.LocationInfoParser
import com.hellguy39.hellweather.data.json.OneCallWeatherParser
import com.hellguy39.hellweather.data.local.WeatherDatabase
import com.hellguy39.hellweather.data.remote.OpenWeatherApi
import com.hellguy39.hellweather.data.repository.LocalRepositoryImpl
import com.hellguy39.hellweather.data.repository.RemoteRepositoryImpl
import com.hellguy39.hellweather.domain.repository.LocalRepository
import com.hellguy39.hellweather.domain.repository.RemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRemoteRepository(
        api: OpenWeatherApi,
        oneCallWeatherParser: OneCallWeatherParser,
        locationInfoParser: LocationInfoParser
    ): RemoteRepository {
        return RemoteRepositoryImpl (
            api = api,
            locationInfoParser = locationInfoParser,
            oneCallWeatherParser = oneCallWeatherParser
        )
    }

    @Provides
    @Singleton
    fun provideLocalRepository(
        db: WeatherDatabase,
        oneCallWeatherParser: OneCallWeatherParser,
        locationInfoParser: LocationInfoParser
    ): LocalRepository {
        return LocalRepositoryImpl(
            db.dao,
            oneCallWeatherParser,
            locationInfoParser
        )
    }
}