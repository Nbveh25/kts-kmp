import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    //alias(libs.plugins.koinCompilerPlugin)
    //alias(libs.plugins.buildKonfigPlugin)
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
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {

            api(projects.shared.main)

            api(projects.shared.core.ui)
            api(projects.shared.core.theme)
            api(projects.shared.core.network)

            api(projects.shared.feature.onboarding)
            api(projects.shared.feature.auth.impl)
            api(projects.shared.feature.main.impl)
        }
    }

}

android {
    namespace = "ru.kazan.itis.bikmukhametov.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
