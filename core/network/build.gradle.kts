import com.hellguy39.hellweather.buildsrc.*
import com.hellguy39.hellweather.buildsrc.install.installCoroutines
import com.hellguy39.hellweather.buildsrc.install.installKoin
import com.hellguy39.hellweather.buildsrc.install.installKtor
import com.hellguy39.hellweather.buildsrc.install.installTestingTools

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("java-compile-plugin")
    id("default-config-plugin")
    id("kotlinx-serialization")
}

android {
    namespace = "com.hellguy39.hellweather.core.network"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildFeatures.buildConfig = true
        }
    }

    buildTypes.forEach { buildType ->
        buildType.buildConfigField(String::class.java.typeName, "BASE_URL", "\"https://api.weatherapi.com/\"")
        buildType.buildConfigField(String::class.java.typeName, "API_KEY", "\"316f82947d7045dd8e483252230208\"")
    }

}

installKoin()
installCoroutines()
installKtor()
installTestingTools()

dependencies {

    implementation(project(Modules.Core.Model))

}