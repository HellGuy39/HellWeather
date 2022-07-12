package com.hellguy39.hellweather.di

import com.hellguy39.hellweather.domain.repository.LocalRepository
import com.hellguy39.hellweather.domain.repository.RemoteRepository
import com.hellguy39.hellweather.domain.usecase.GetCachedForecastUseCase
import com.hellguy39.hellweather.domain.usecase.GetWeatherForecastUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    
    @Singleton
    @Provides
    fun provideGetWeatherForecastUseCase(
        remoteRepository: RemoteRepository,
        localRepository: LocalRepository
    ): GetWeatherForecastUseCase {
        return GetWeatherForecastUseCase(
            remoteRepo = remoteRepository,
            localRepo = localRepository
        )
    }

    @Singleton
    @Provides
    fun provideGetCachedForecastUseCase(
        localRepository: LocalRepository
    ): GetCachedForecastUseCase {
        return GetCachedForecastUseCase(
            localRepo = localRepository
        )
    }
}