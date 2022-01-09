package com.hellguy39.hellweather.di

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.hellguy39.hellweather.repository.database.LocationDatabase
import com.hellguy39.hellweather.repository.database.LocationRepository
import com.hellguy39.hellweather.repository.database.LocationRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
    fun provideLocationRepository(db: LocationDatabase) : LocationRepository{
        return LocationRepositoryImpl(db.dao)
    }

}