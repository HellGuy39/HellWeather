package com.hellguy39.hellweather.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [])
@Singleton
interface AppComponent {

    fun Context(): Context

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun context(context: Context): Builder
    }

}