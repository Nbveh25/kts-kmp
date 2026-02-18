package ru.kazan.itis.bikmukhametov.kts

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform