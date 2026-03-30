package ru.kazan.itis.bikmukhametov.main.impl.presentation.model

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.DrawableResource
import ru.kazan.itis.bikmukhametov.main.api.model.ChannelKind
import ru.kazan.itis.bikmukhametov.main.api.model.ConversationModel
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_generic_chat_logo
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_jivo_chat_logo
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_max_logo
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_telegram_logo
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_viber_logo
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_vk_logo
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_wazzup_logo
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_widget_logo

@Immutable
data class ConversationCardItem(
    val id: String,
    val avatarUrl: String? = null,
    val socialBadge: DrawableResource,
    val name: String,
    val lastMessageText: String,
    /** ISO 8601 или epoch-ms от API; строка для экрана строится в Composable через formatTimeForUi. */
    val dateUpdatedIso: String,
    val unreadCount: Int = 0,
    // true = ожидает ответа оператора (клиент написал, оператор не ответил)
    val isWaiting: Boolean = false,
    val channelKind: ChannelKind = ChannelKind.UNKNOWN,
    val channelId: String = "",
    val channelName: String? = null,
    /** Идентификатор сегмента/списка из last_message.bucket (если бэкенд отдаёт). */
    val userListBucket: String? = null,
)

internal fun ConversationModel.toConversationCardItem(): ConversationCardItem {
    // Определяем имя пользователя: приоритет: firstName + lastName, иначе username, иначе пусто
    val userName = buildString {
        val firstName = user.firstName
        val lastName = user.lastName
        if (!firstName.isNullOrBlank()) {
            append(firstName)
            if (!lastName.isNullOrBlank()) append(" $lastName")
        } else if (!user.username.isNullOrBlank()) {
            append(user.username)
        } else {
            append("Unknown")
        }
    }

    // Преобразуем kind канала в SocialBadge
    val socialBadgeRes = when (channel.kind) {
        ChannelKind.JIVO -> Res.drawable.ic_jivo_chat_logo
        ChannelKind.MAX -> Res.drawable.ic_max_logo
        ChannelKind.TG -> Res.drawable.ic_telegram_logo
        ChannelKind.VB -> Res.drawable.ic_viber_logo
        ChannelKind.WZ -> Res.drawable.ic_wazzup_logo
        ChannelKind.WIDGET -> Res.drawable.ic_widget_logo
        ChannelKind.VK -> Res.drawable.ic_vk_logo
        else -> Res.drawable.ic_generic_chat_logo
    }

    // Текст последнего сообщения или пустая строка
    val lastMessageText = lastMessage?.text ?: ""

    // Количество непрочитанных: если диалог не прочитан, считаем 1 (иначе 0)
    // Это временное решение, так как API не возвращает точное число непрочитанных
    val unreadCount = if (!isRead) 1 else 0

    // Ждёт ответа оператора: диалог активен И нет непрочитанного сообщения от оператора
    // (клиент написал последним, оператор ещё не ответил)
    val isWaiting = !state.stoppedByManager && !state.hasUnansweredOperatorMessage

    return ConversationCardItem(
        id = id.toString(),
        avatarUrl = user.photo?.url,
        socialBadge = socialBadgeRes,
        name = userName,
        lastMessageText = lastMessageText,
        dateUpdatedIso = dateUpdated,
        unreadCount = unreadCount,
        isWaiting = isWaiting,
        channelKind = channel.kind,
        channelId = channel.id,
        channelName = channel.name,
        userListBucket = lastMessage?.bucket,
    )
}
