package com.hellguy39.hellweather.di

import android.app.Application
import androidx.room.Room
import com.hellguy39.hellweather.data.json.LocationInfoParser
import com.hellguy39.hellweather.data.json.OneCallWeatherParser
import com.hellguy39.hellweather.data.local.WeatherDatabase
import com.hellguy39.hellweather.data.remote.OpenWeatherApi
import com.hellguy39.hellweather.data.repository.LocalRepositoryImpl
import com.hellguy39.hellweather.data.repository.RemoteRepositoryImpl
import com.hellguy39.hellweather.domain.repository.LocalRepository
import com.hellguy39.hellweather.domain.repository.RemoteRepository
import com.hellguy39.hellweather.domain.usecase.GetWeatherForecastUseCase
import com.squareup.moshi.Moshi
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
    fun provideDatabase(app: Application): WeatherDatabase {
        return Room.databaseBuilder(
            app,
            WeatherDatabase::class.java,
            "weatherdb.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    fun provideOneCallWeatherParser(moshi: Moshi): OneCallWeatherParser {
        return OneCallWeatherParser(moshi)
    }

    @Provides
    @Singleton
    fun provideLocationNameParser(moshi: Moshi): LocationInfoParser {
        return LocationInfoParser(moshi)
    }


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

}