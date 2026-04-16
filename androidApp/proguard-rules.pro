# R8: обфускация имён + удаление мёртвого кода (isMinifyEnabled).
# Дополнительные правила под Kotlin, KMP-зависимости и Crashlytics.

# --- Читаемые стеки в Crashlytics при обфускации ---
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

-keepattributes Signature,*Annotation*,InnerClasses,EnclosingMethod

# --- Kotlin / coroutines (Koin, Ktor) ---
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.reflect.jvm.internal.**

# --- kotlinx.serialization ---
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class **$$serializer {
    *;
}
-keepclassmembers class **$$serializer {
    *;
}

# --- Koin ---
-keep class org.koin.** { *; }
-keep class * extends org.koin.core.module.Module

# --- Ktor / OkHttp / сетевые предупреждения ---
-dontwarn io.ktor.**
-dontwarn okhttp3.**
-dontwarn okio.**

# --- Coil ---
-keep class coil3.** { *; }

# --- Room (если используется в релизе) ---
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
