package com.hellguy39.hellweather.di

import android.app.Application
import androidx.room.Room
import com.hellguy39.hellweather.data.db.LocationDatabase
import com.hellguy39.hellweather.domain.repository.LocationRepository
import com.hellguy39.hellweather.data.repositories.LocationRepositoryImpl
import com.hellguy39.hellweather.domain.usecase.local.AddUserLocationUseCase
import com.hellguy39.hellweather.domain.usecase.local.DeleteUserLocationUseCase
import com.hellguy39.hellweather.domain.usecase.local.GetUserLocationListUseCase
import com.hellguy39.hellweather.domain.usecase.local.UserLocationUseCases
import com.hellguy39.hellweather.domain.usecase.prefs.service.ServiceUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Provides
    @Singleton
    fun provideLocationDatabase(app: Application) : LocationDatabase {
        return Room.databaseBuilder(
            app,
            LocationDatabase::class.java,
            "location_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLocationRepository(db: LocationDatabase) : LocationRepository {
        return LocationRepositoryImpl(db.dao)
    }

    @Provides
    @Singleton
    fun provideUserLocationUseCases(locationRepositoryImpl: LocationRepository): UserLocationUseCases {
        return UserLocationUseCases(
            addUserLocationUseCase = AddUserLocationUseCase(locationRepositoryImpl),
            deleteUserLocationUseCase = DeleteUserLocationUseCase(locationRepositoryImpl),
            getUserLocationListUseCase = GetUserLocationListUseCase(locationRepositoryImpl)
        )
    }
}