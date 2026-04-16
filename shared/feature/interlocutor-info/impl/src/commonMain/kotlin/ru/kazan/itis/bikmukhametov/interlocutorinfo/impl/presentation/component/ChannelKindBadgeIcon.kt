package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.mapper.drawableResourceForChannelKind
import ru.kazan.itis.bikmukhametov.main.api.model.ChannelKind

@Composable
internal fun ChannelKindBadgeIcon(channelKind: ChannelKind) {
    Icon(
        painter = painterResource(drawableResourceForChannelKind(channelKind)),
        contentDescription = null,
        modifier = Modifier.size(40.dp),
        tint = Color.Unspecified,
    )
}
