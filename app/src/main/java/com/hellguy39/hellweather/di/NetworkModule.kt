package com.hellguy39.hellweather.di

import com.hellguy39.hellweather.data.repositories.ApiRepositoryImpl
import com.hellguy39.hellweather.data.api.ApiService
import com.hellguy39.hellweather.domain.usecase.requests.location.GetLocationByCityNameUseCase
import com.hellguy39.hellweather.domain.usecase.requests.location.GetLocationByCoordsUseCase
import com.hellguy39.hellweather.domain.usecase.requests.location.LocationRequestUseCases
import com.hellguy39.hellweather.domain.usecase.requests.weather.GetCurrentWeatherByCityNameUseCase
import com.hellguy39.hellweather.domain.usecase.requests.weather.GetCurrentWeatherByCoordsUseCase
import com.hellguy39.hellweather.domain.usecase.requests.weather.GetOneCallWeatherUseCase
import com.hellguy39.hellweather.domain.usecase.requests.weather.WeatherRequestUseCases
import com.hellguy39.hellweather.domain.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return retrofitClient().create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun retrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiRepository(apiService: ApiService): ApiRepositoryImpl {
        return ApiRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideLocationRequestUseCases(apiRepositoryImpl: ApiRepositoryImpl): LocationRequestUseCases {
        return LocationRequestUseCases(
            getLocationByCityNameUseCase = GetLocationByCityNameUseCase(apiRepositoryImpl),
            getLocationByCoordsUseCase = GetLocationByCoordsUseCase(apiRepositoryImpl)
        )
    }

    @Provides
    @Singleton
    fun provideRequestUseCases(apiRepositoryImpl: ApiRepositoryImpl): WeatherRequestUseCases {
        return WeatherRequestUseCases(
            getOneCallWeatherUseCase = GetOneCallWeatherUseCase(apiRepositoryImpl),
            getCurrentWeatherByCityNameUseCase = GetCurrentWeatherByCityNameUseCase(apiRepositoryImpl),
            getCurrentWeatherByCoordsUseCase = GetCurrentWeatherByCoordsUseCase(apiRepositoryImpl)
        )
    }

}