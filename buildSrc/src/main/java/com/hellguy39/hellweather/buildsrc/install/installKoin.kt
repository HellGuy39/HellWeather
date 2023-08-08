package com.hellguy39.hellweather.buildsrc.install

import com.hellguy39.hellweather.buildsrc.Libs
import com.hellguy39.hellweather.buildsrc.util.Configuration
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.installKoin() {

    dependencies {
        add(Configuration.Implementation, Libs.Koin.Core)
        add(Configuration.Implementation, Libs.Koin.Android)
        add(Configuration.Implementation, Libs.Koin.AndroidXCompose)
        add(Configuration.Implementation, Libs.Koin.AndroidXComposeNavigation)
        add(Configuration.Implementation, Libs.Koin.AndroidXWorkManager)
        add(Configuration.Implementation, Libs.Koin.Ktor)
        add(Configuration.Implementation, Libs.Koin.SLF4JLogger)
    }
}