package com.hellguy39.hellweather.di

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.hellguy39.hellweather.App
import com.hellguy39.hellweather.data.repositories.PrefsRepositoryImpl
import com.hellguy39.hellweather.domain.usecase.prefs.first_boot.FirstBootValueUseCases
import com.hellguy39.hellweather.domain.usecase.prefs.first_boot.GetFirstBootValueUseCase
import com.hellguy39.hellweather.domain.usecase.prefs.first_boot.SaveFirstBootValueUseCase
import com.hellguy39.hellweather.domain.usecase.prefs.lang.GetLangUseCase
import com.hellguy39.hellweather.domain.usecase.prefs.lang.LangUseCases
import com.hellguy39.hellweather.domain.usecase.prefs.lang.SaveLangUseCase
import com.hellguy39.hellweather.domain.usecase.prefs.service.*
import com.hellguy39.hellweather.domain.usecase.prefs.units.GetUnitsUseCase
import com.hellguy39.hellweather.domain.usecase.prefs.units.SaveUnitsUseCase
import com.hellguy39.hellweather.domain.usecase.prefs.units.UnitsUseCases
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
    fun provideGetLangUseCase(prefsRepositoryImpl: PrefsRepositoryImpl): LangUseCases {
        return LangUseCases(
            getLangUseCase = GetLangUseCase(),
            saveLangUseCase = SaveLangUseCase()
        )
    }

    @Provides
    @Singleton
    fun provideUnitsUseCases(prefsRepositoryImpl: PrefsRepositoryImpl): UnitsUseCases {
        return UnitsUseCases(
            getUnitsUseCase = GetUnitsUseCase(prefsRepositoryImpl),
            saveUnitsUseCase = SaveUnitsUseCase(prefsRepositoryImpl)
        )
    }

    @Provides
    @Singleton
    fun provideFirstBootValueUseCases(prefsRepositoryImpl: PrefsRepositoryImpl): FirstBootValueUseCases {
        return FirstBootValueUseCases(
            getFirstBootValueUseCase = GetFirstBootValueUseCase(prefsRepositoryImpl),
            saveFirstBootValueUseCase = SaveFirstBootValueUseCase(prefsRepositoryImpl)
        )
    }

    @Provides
    @Singleton
    fun provideServiceUseCases(prefsRepositoryImpl: PrefsRepositoryImpl): ServiceUseCases {
        return ServiceUseCases(
            saveServiceLocationUseCase = SaveServiceLocationUseCase(prefsRepositoryImpl),
            saveServiceModeUseCase = SaveServiceModeUseCase(prefsRepositoryImpl),
            saveServiceUpdateTimeUseCase = SaveServiceUpdateTimeUseCase(prefsRepositoryImpl),
            getServiceLocationUseCase = GetServiceLocationUseCase(prefsRepositoryImpl),
            getServiceModeUseCase = GetServiceModeUseCase(prefsRepositoryImpl),
            getServiceUpdateTimeUseCase = GetServiceUpdateTimeUseCase(prefsRepositoryImpl)
        )
    }

}