package com.hellguy39.hellweather.buildsrc.install

import com.android.build.gradle.BaseExtension
import com.hellguy39.hellweather.buildsrc.Libs
import com.hellguy39.hellweather.buildsrc.util.Configuration
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.installRealm() {

    plugins.apply("io.realm.kotlin")

    dependencies {
        add(Configuration.Implementation, Libs.Realm.Base)
        add(Configuration.Implementation, Libs.Realm.Sync)
    }
}