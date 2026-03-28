package ru.kazan.itis.bikmukhametov.profile.impl.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.kazan.itis.bikmukhametov.profile.impl.presentation.component.ProfileLogoutButton
import ru.kazan.itis.bikmukhametov.profile.impl.presentation.component.ProfileNotificationsCard
import ru.kazan.itis.bikmukhametov.profile.impl.presentation.component.ProfileUserCard
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun ProfileContent(
    state: ProfileUiState,
    onToggleNotifications: (Boolean) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Spacing.paddingMedium, vertical = Spacing.paddingMedium),
        verticalArrangement = Arrangement.spacedBy(Spacing.paddingMedium)
    ) {
        ProfileUserCard(profile = state.profile)

        HorizontalDivider()

        ProfileNotificationsCard(
            notificationsEnabled = state.notificationsEnabled,
            onToggle = onToggleNotifications
        )

        HorizontalDivider()

        Spacer(modifier = Modifier.weight(1f))

        ProfileLogoutButton(
            isLoggingOut = state.isLoggingOut,
            onLogout = onLogout
        )
    }
}
