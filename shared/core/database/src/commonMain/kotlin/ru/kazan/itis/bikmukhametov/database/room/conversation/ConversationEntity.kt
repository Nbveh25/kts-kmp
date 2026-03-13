package ru.kazan.itis.bikmukhametov.database.room.conversation

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey val id: Long,
    val dateUpdated: String,
    val isRead: Boolean,

    // User
    val userId: String,
    val userUsername: String?,
    val userFirstName: String?,
    val userLastName: String?,
    val userPhotoUrl: String?,
    val userUrl: String?,

    // Channel
    val channelId: String,
    val channelKind: String,
    val channelName: String?,
    val channelUrl: String?,

    // State
    val stoppedByManager: Boolean,
    val operatorTagged: Boolean,
    val hasUnansweredOperatorMessage: Boolean,

    // Last message
    val lastMessageId: String?,
    val lastMessageConversationId: Long?,
    val lastMessageText: String?,
    val lastMessageKind: String?,
    val lastMessageBlockId: String?,
    val lastMessageScenarioId: String?,
    val lastMessageBucket: String?,
    val lastMessageDateCreated: String?,
    val lastMessageManagerEmail: String?,
    val lastMessageIsRead: Boolean?,

    // Last message attachment
    val attachmentAsDocument: Boolean?,
    val attachmentFilename: String?,
    val attachmentPreviewUrl: String?,
    val attachmentSize: Int?,
    val attachmentType: String?,
    val attachmentUrl: String?,
    val attachmentWidth: Int?,
    val attachmentHeight: Int?,
)
