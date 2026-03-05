import org.gradle.kotlin.dsl.implementation
import org.gradle.kotlin.dsl.project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "FeatureLoginImpl"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {

            implementation(project(":shared:core:ui"))
            implementation(project(":shared:core:theme"))
            implementation(project(":shared:feature:auth:api"))

            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.components.resources)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
        }
    }
}

compose.resources {
    packageOfResClass = "ru.kazan.itis.bikmukhametov.impl.generated.resources"
}

android {
    namespace = "ru.kazan.itis.bikmukhametov.impl"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
