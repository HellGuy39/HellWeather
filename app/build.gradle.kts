import com.hellguy39.hellweather.buildsrc.*
import com.hellguy39.hellweather.buildsrc.install.installAndroidCore
import com.hellguy39.hellweather.buildsrc.install.installCompose
import com.hellguy39.hellweather.buildsrc.install.installCoroutines
import com.hellguy39.hellweather.buildsrc.install.installKoin
import com.hellguy39.hellweather.buildsrc.install.installTestingTools

plugins {
    id("com.android.application")
    kotlin("android")
    id("java-compile-plugin")
    id("default-config-plugin")
    id("kotlinx-serialization")
}

android {

    namespace = "com.hellguy39.hellweather"

    defaultConfig {
        applicationId = "com.hellguy39.hellweather"
    }

    applicationVariants.all {
        val isFirebaseEnabled =
            if (this.name == "release" || this.name == "beta") true.toString() else false.toString()

        buildConfigField(Boolean::class.java.typeName, "ENABLE_CRASHLYTICS", isFirebaseEnabled)
        buildConfigField(Boolean::class.java.typeName, "ENABLE_ANALYTICS", isFirebaseEnabled)
    }

    namespace = "com.hellguy39.hellweather"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

installAndroidCore()
installCompose()
installCoroutines()
installKoin()
installTestingTools()

dependencies {
    implementation(Libs.PlayServices.Location)

    implementation(project(Modules.Core.Data))
    implementation(project(Modules.Core.Common))
    implementation(project(Modules.Core.Domain))
    implementation(project(Modules.Core.Model))
    implementation(project(Modules.Core.Ui))
    implementation(project(Modules.Core.Network))
}