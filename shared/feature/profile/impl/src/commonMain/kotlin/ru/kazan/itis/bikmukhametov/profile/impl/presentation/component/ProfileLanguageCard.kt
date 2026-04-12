package ru.kazan.itis.bikmukhametov.profile.impl.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import ru.kazan.itis.bikmukhametov.database.locale.AppLanguage
import ru.kazan.itis.bikmukhametov.profile.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.profile.impl.generated.resources.profile_language_english
import ru.kazan.itis.bikmukhametov.profile.impl.generated.resources.profile_language_russian
import ru.kazan.itis.bikmukhametov.profile.impl.generated.resources.profile_language_title
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun ProfileLanguageCard(
    selected: AppLanguage,
    onSelect: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.paddingSmall),
    ) {
        Text(
            text = stringResource(Res.string.profile_language_title),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.paddingSmall),
        ) {
            FilterChip(
                selected = selected == AppLanguage.RU,
                onClick = { onSelect(AppLanguage.RU) },
                label = { Text(stringResource(Res.string.profile_language_russian)) },
            )
            FilterChip(
                selected = selected == AppLanguage.EN,
                onClick = { onSelect(AppLanguage.EN) },
                label = { Text(stringResource(Res.string.profile_language_english)) },
            )
        }
    }
}
