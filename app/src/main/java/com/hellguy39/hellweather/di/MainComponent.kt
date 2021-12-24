package com.hellguy39.hellweather.di

import com.hellguy39.hellweather.ui.main.MainFragment
import dagger.Component
/*
@ScreenScope
@Component(
    dependencies = [AppComponent::class],
    modules = [MainViewModelModule::class]
)

interface MainComponent {
    fun inject(mainFragment: MainFragment)

    @Component.Builder
    interface Builder {
        fun appComponent(appComponent: AppComponent): Builder
        fun build(): MainComponent
    }

    companion object {
        fun init(appComponent: AppComponent): MainComponent {
            return DaggerMainComponent
                .builder()
                .appCompanent(appComponent)
                .build()
        }
    }

}*/