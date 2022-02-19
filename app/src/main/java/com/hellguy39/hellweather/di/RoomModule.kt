package com.hellguy39.hellweather.di

import android.app.Application
import androidx.room.Room
import com.hellguy39.hellweather.data.db.LocationDatabase
import com.hellguy39.hellweather.data.repositories.LocationRepository
import com.hellguy39.hellweather.data.repositories.LocationRepositoryImpl
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

}