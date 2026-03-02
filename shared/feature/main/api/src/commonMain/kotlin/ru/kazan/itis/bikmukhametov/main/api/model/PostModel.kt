package ru.kazan.itis.bikmukhametov.main.api.model

data class PostModel(
    val id: Long,
    val title: String,
    val subtitle: String,
    val description: String,
    val imageUrl: String,
)