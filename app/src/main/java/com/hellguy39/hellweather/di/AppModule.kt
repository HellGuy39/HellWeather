package com.hellguy39.hellweather.di

import com.hellguy39.hellweather.data.remote.OpenWeatherApi
import com.hellguy39.hellweather.data.repository.OpenWeatherRepositoryImpl
import com.hellguy39.hellweather.domain.repository.OpenWeatherRepository
import com.hellguy39.hellweather.domain.usecase.GetOneCallWeatherUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOpenWeatherApi(): OpenWeatherApi {
        return Retrofit.Builder()
            .baseUrl(OpenWeatherApi.BASE_URL)
            .client(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }).build())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideOpenWeatherRepository(api: OpenWeatherApi): OpenWeatherRepository {
        return OpenWeatherRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideGetOneCallWeatherUseCase(repository: OpenWeatherRepository): GetOneCallWeatherUseCase {
        return GetOneCallWeatherUseCase(
            repository
        )
    }

}