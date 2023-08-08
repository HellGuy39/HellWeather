package com.hellguy39.hellweather.buildsrc.install

import com.hellguy39.hellweather.buildsrc.Libs
import com.hellguy39.hellweather.buildsrc.util.Configuration
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.installCoroutines() {
    dependencies {
        add(Configuration.Implementation, Libs.Kotlin.Coroutines)
    }
}