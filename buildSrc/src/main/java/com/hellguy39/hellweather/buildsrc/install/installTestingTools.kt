package com.hellguy39.hellweather.buildsrc.install

import com.hellguy39.hellweather.buildsrc.Libs
import com.hellguy39.hellweather.buildsrc.util.Configuration
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.installTestingTools() {
    dependencies {
        add(Configuration.AndroidTestImplementation, Libs.Testing.Work)
        add(Configuration.AndroidTestImplementation, Libs.Testing.UiTestJUnit)
        add(Configuration.DebugImplementation, Libs.Testing.UiTooling)
        add(Configuration.DebugImplementation, Libs.Testing.UiTestManifest)
        add(Configuration.TestImplementation, Libs.Testing.JUnit)
        add(Configuration.AndroidTestImplementation, Libs.Testing.AndroidJUnit)
        add(Configuration.AndroidTestImplementation, Libs.Testing.Espresso)
    }
}