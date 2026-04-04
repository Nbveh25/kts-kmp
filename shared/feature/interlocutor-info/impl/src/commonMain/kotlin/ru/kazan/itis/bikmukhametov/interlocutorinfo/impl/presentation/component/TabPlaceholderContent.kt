package ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.interlocutorinfo.impl.generated.resources.ic_tab_placeholder
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun TabPlaceholderContent(
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(Spacing.paddingMedium)
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_tab_placeholder),
                contentDescription = null,
                modifier = Modifier.size(width = 48.dp, height = 37.dp),
                contentScale = ContentScale.Fit,
            )

            Spacer(modifier = Modifier.height(Spacing.paddingMedium))

            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
        }
    }
}
