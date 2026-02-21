package ru.kazan.itis.bikmukhametov.kts.presentation.theme

import androidx.compose.ui.graphics.Color

// Основные брендовые цвета KTS Metaclass
val KtsViolet = Color(0xFF7B61FF)     // Основной фиолетовый (кнопки, акценты)
val KtsGreen = Color(0xFF3DDC84)      // Тот самый Android-зеленый, который они используют
val KtsGraphite = Color(0xFF1F1F1F)   // Цвет темного текста и элементов

// Светлая тема
val KtsBgLight = Color(0xFFFFFFFF)
val KtsSurfaceLight = Color(0xFFF5F5F7) // Светло-серые блоки
val KtsTextPrimary = Color(0xFF000000)
val KtsTextSecondary = Color(0xFF6E6E73)

// Темная тема (как на главной странице сайта)
val KtsBgDark = Color(0xFF000000)       // Сайт KTS глубоко черный
val KtsSurfaceDark = Color(0xFF1C1C1E)  // Карточки в темной теме

// Контент поверх акцентных цветов (для colorScheme)
val KtsOnPrimary = Color(0xFFFFFFFF)       // текст/иконки на primary (обе темы)
val KtsOnSecondaryDark = Color(0xFF000000) // контент на secondary в тёмной теме
val KtsOnBackgroundDark = Color(0xFFFFFFFF) // текст на background в тёмной теме
val KtsOnSurfaceDark = Color(0xFFFFFFFF)   // текст на surface в тёмной теме
