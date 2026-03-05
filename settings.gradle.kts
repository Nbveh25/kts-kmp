rootProject.name = "KTS"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

// Точки входа
include(":androidApp")
include(":iosApp")

// Зонтик объединяет main (навигация, App) + core + feature
include(":shared")
include(":shared:main")

include(":shared:core:ui")
include(":shared:core:theme")
include(":shared:core:network")

include(":shared:feature:onboarding")

include(":shared:feature:auth:api")
include(":shared:feature:auth:impl")

include(":shared:feature:main:api")
include(":shared:feature:main:impl")
