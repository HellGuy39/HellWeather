package com.hellguy39.hellweather.di

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.hellguy39.hellweather.App
import com.hellguy39.hellweather.data.repositories.PrefsRepositoryImpl
import com.hellguy39.hellweather.domain.usecase.prefs.first_boot.GetFirstBootValueUseCase
import com.hellguy39.hellweather.domain.usecase.prefs.first_boot.SaveFirstBootValueUseCase
import com.hellguy39.hellweather.domain.usecase.prefs.service.*
import com.hellguy39.hellweather.domain.usecase.prefs.units.GetUnitsUseCase
import com.hellguy39.hellweather.domain.usecase.prefs.units.SaveUnitsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun defSharedPrefs(app: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(app)
    }

    @Provides
    @Singleton
    fun providePrefsRepository(sharedPreferences: SharedPreferences): PrefsRepositoryImpl {
        return PrefsRepositoryImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideGetUnitsUseCase(prefsRepositoryImpl: PrefsRepositoryImpl): GetUnitsUseCase {
        return GetUnitsUseCase(prefsRepositoryImpl)
    }


    @Provides
    @Singleton
    fun provideSaveUnitsUseCase(prefsRepositoryImpl: PrefsRepositoryImpl): SaveUnitsUseCase {
        return SaveUnitsUseCase(prefsRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideGetFirstBootValueUseCase(prefsRepositoryImpl: PrefsRepositoryImpl): GetFirstBootValueUseCase {
        return GetFirstBootValueUseCase(prefsRepositoryImpl)
    }


    @Provides
    @Singleton
    fun provideSaveFirstBootValueUseCase(prefsRepositoryImpl: PrefsRepositoryImpl): SaveFirstBootValueUseCase {
        return SaveFirstBootValueUseCase(prefsRepositoryImpl)
    }

    // Service

    @Provides
    @Singleton
    fun provideGetServiceModeUseCase(prefsRepositoryImpl: PrefsRepositoryImpl): GetServiceModeUseCase{
        return GetServiceModeUseCase(prefsRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideGetServiceLocationUseCase(prefsRepositoryImpl: PrefsRepositoryImpl): GetServiceLocationUseCase{
        return GetServiceLocationUseCase(prefsRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideGetServiceUpdateTimeUseCase(prefsRepositoryImpl: PrefsRepositoryImpl): GetServiceUpdateTimeUseCase{
        return GetServiceUpdateTimeUseCase(prefsRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideSaveServiceModeUseCase(prefsRepositoryImpl: PrefsRepositoryImpl): SaveServiceModeUseCase {
        return SaveServiceModeUseCase(prefsRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideSaveServiceLocationUseCase(prefsRepositoryImpl: PrefsRepositoryImpl): SaveServiceLocationUseCase {
        return SaveServiceLocationUseCase(prefsRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideSaveServiceUpdateTimeUseCase(prefsRepositoryImpl: PrefsRepositoryImpl): SaveServiceUpdateTimeUseCase{
        return SaveServiceUpdateTimeUseCase(prefsRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideServiceUseCases(
        saveServiceUserLocation: SaveServiceLocationUseCase,
        saveServiceModeUseCase: SaveServiceModeUseCase,
        saveServiceUpdateTimeUseCase: SaveServiceUpdateTimeUseCase,
        getServiceModeUseCase: GetServiceModeUseCase,
        getServiceLocationUseCase: GetServiceLocationUseCase,
        getServiceUpdateTimeUseCase: GetServiceUpdateTimeUseCase
    ): ServiceUseCases {
        return ServiceUseCases(
            saveServiceLocationUseCase = saveServiceUserLocation,
            saveServiceModeUseCase = saveServiceModeUseCase,
            saveServiceUpdateTimeUseCase = saveServiceUpdateTimeUseCase,
            getServiceLocationUseCase = getServiceLocationUseCase,
            getServiceModeUseCase = getServiceModeUseCase,
            getServiceUpdateTimeUseCase = getServiceUpdateTimeUseCase
        )
    }

}