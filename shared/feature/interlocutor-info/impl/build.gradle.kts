import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serializationPlugin)
    alias(libs.plugins.buildKonfigPlugin)
}

fun localProperty(key: String): String {
    val file = rootProject.file("local.properties")
    if (!file.isFile) return ""
    val props = Properties()
    FileInputStream(file).use { props.load(it) }
    return props.getProperty(key) ?: ""
}

buildkonfig {
    packageName = "ru.kazan.itis.bikmukhametov.interlocutorinfo.impl"
    defaultConfigs {
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "BASE_URL",
            value = "https://${localProperty("CABINET_DOMAIN")}.smartbotpro.ru",
        )
    }
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
            baseName = "FeatureInterlocutorInfoImpl"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared.feature.interlocutorInfo.api)
            implementation(projects.shared.feature.main.api)
            implementation(projects.shared.core.designsystem)
            implementation(projects.shared.core.network)

            implementation(libs.ktor.client.core)

            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.components.resources)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.napier)
        }
        androidMain.dependencies {}
    }
}

compose.resources {
    packageOfResClass = "ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources"
}

android {
    namespace = "ru.kazan.itis.bikmukhametov.interlocutorinfo.impl"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
