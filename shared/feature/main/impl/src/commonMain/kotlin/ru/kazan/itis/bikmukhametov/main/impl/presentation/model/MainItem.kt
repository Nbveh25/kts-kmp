package ru.kazan.itis.bikmukhametov.main.impl.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class MainItem(
    val id: Long,
    val title: String,
    val subtitle: String,
    val description: String,
    val imageUrl: String,
)
