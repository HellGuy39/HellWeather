import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        register("java-compile-plugin") {
            id = "java-compile-plugin"
            implementationClass = "com.hellguy39.hellweather.buildsrc.plugin.JavaCompilePlugin"
        }
        register("default-config-plugin") {
            id = "default-config-plugin"
            implementationClass = "com.hellguy39.hellweather.buildsrc.plugin.DefaultConfigPlugin"
        }
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())

    implementation("com.android.tools.build:gradle:8.1.0")
    implementation(kotlin("gradle-plugin", "1.9.0"))
    implementation(kotlin("android-extensions"))
    implementation("com.squareup:javapoet:1.13.0")
}