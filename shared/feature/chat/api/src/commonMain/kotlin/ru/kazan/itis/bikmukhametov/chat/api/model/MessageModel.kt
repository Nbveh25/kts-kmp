package ru.kazan.itis.bikmukhametov.chat.api.model

data class MessageModel(
    val id: String,
    val conversationId: String,
    val text: String,
    val senderId: String,
    val createdAt: Long,
)
