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

            api(project(":shared:main"))

            api(project(":shared:core:ui"))
            api(project(":shared:core:theme"))
            api(project(":shared:core:network"))

            api(project(":shared:feature:onboarding"))
            api(project(":shared:feature:auth:impl"))
            api(project(":shared:feature:main:impl"))
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
