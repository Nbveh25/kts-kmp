import org.gradle.internal.extensions.core.extra

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false

    alias(libs.plugins.detektPlugin) apply false
    alias(libs.plugins.serializationPlugin) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.jetbrainsKotlinJvm) apply false
}

private val versionMajor = 1
private val versionMinor = 0

val versionName by extra(initialValue = "$versionMajor.$versionMinor")
val versionCode by extra(initialValue = versionMajor * 1000 + versionMinor * 10)

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension>("detekt") {
        buildUponDefaultConfig = true
        config.setFrom(files("$rootDir/config/detekt/detekt.yml"))

        // Явно указываем исходники для детекта, включая KMP-источники
        source.setFrom(
            files(
                "src/main/java",
                "src/main/kotlin",
                "src/commonMain/kotlin",
                "src/androidMain/kotlin",
                "src/iosMain/kotlin",
            )
        )
    }
    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = "11"
        // Не сканировать сгенерированный/собранный код
        exclude("**/build/**")
    }
}
