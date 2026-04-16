package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.mapper

import org.jetbrains.compose.resources.DrawableResource
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.ic_generic_chat_logo
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.ic_jivo_chat_logo
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.ic_max_logo
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.ic_telegram_logo
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.ic_viber_logo
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.ic_vk_logo
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.ic_wazzup_logo
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.ic_widget_logo
import ru.kazan.itis.bikmukhametov.main.api.model.ChannelKind

/** Сырая строка `channel.kind` из чат-API */
internal fun channelKindFromApiRaw(raw: String): ChannelKind {
    val normalized = raw.lowercase().trim()
    return when (normalized) {
        "telegram" -> ChannelKind.TG
        else -> ChannelKind.fromString(raw)
    }
}

/**
 * `ChannelKind` → иконка канала в ресурсах модуля.
 */
internal fun drawableResourceForChannelKind(kind: ChannelKind): DrawableResource =
    when (kind) {
        ChannelKind.JIVO -> Res.drawable.ic_jivo_chat_logo
        ChannelKind.MAX -> Res.drawable.ic_max_logo
        ChannelKind.TG -> Res.drawable.ic_telegram_logo
        ChannelKind.VB -> Res.drawable.ic_viber_logo
        ChannelKind.WZ -> Res.drawable.ic_wazzup_logo
        ChannelKind.WIDGET -> Res.drawable.ic_widget_logo
        ChannelKind.VK -> Res.drawable.ic_vk_logo
        else -> Res.drawable.ic_generic_chat_logo
    }
