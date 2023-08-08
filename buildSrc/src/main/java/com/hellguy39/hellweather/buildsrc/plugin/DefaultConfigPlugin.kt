package com.hellguy39.hellweather.buildsrc.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class DefaultConfigPlugin: Plugin<Project> {

    override fun apply(project: Project) {

        val androidExtension = project.extensions.getByName("android")

        if (androidExtension !is BaseExtension)
            return

        androidExtension.apply {
            compileSdkVersion(34) // Android 14

            defaultConfig {
                minSdk = 29 // Android 10
                targetSdk = 34 // Android 14
                versionCode = 4
                versionName = "2.0-alpha01" // X.Y.Z; X = Major, Y = minor, Z = Patch level

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                vectorDrawables {
                    useSupportLibrary = true
                }
            }
        }
    }
}