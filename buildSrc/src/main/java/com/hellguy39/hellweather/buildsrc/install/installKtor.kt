package com.hellguy39.hellweather.buildsrc.install

import com.hellguy39.hellweather.buildsrc.Libs
import com.hellguy39.hellweather.buildsrc.util.Configuration
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.installKtor() {

    dependencies {
        add(Configuration.Implementation, Libs.Ktor.Android)
        add(Configuration.Implementation, Libs.Ktor.Core)
        add(Configuration.Implementation, Libs.Ktor.ContentNegotiation)
        add(Configuration.Implementation, Libs.Ktor.Logging)
        add(Configuration.Implementation, Libs.Ktor.JsonSerializer)
    }
}