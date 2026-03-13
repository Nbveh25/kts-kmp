package ru.kazan.itis.bikmukhametov.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ru.kazan.itis.bikmukhametov.ui.generated.resources.Res
import ru.kazan.itis.bikmukhametov.ui.generated.resources.app_bottom_nav_chats
import ru.kazan.itis.bikmukhametov.ui.generated.resources.app_bottom_nav_profile
import ru.kazan.itis.bikmukhametov.ui.generated.resources.ic_account_circle_24
import ru.kazan.itis.bikmukhametov.ui.generated.resources.ic_chat_24

@Composable
fun AppBottomNav(
    chatsSelected: Boolean,
    onChatsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    NavigationBar {
        NavigationBarItem(
            selected = chatsSelected,
            onClick = onChatsClick,
            icon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_chat_24),
                    contentDescription = stringResource(Res.string.app_bottom_nav_chats)
                )
            },
            label = {
                Text(
                    text = stringResource(Res.string.app_bottom_nav_chats)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        NavigationBarItem(
            selected = !chatsSelected,
            onClick = onProfileClick,
            icon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_account_circle_24),
                    contentDescription = stringResource(Res.string.app_bottom_nav_profile)
                )
            },
            label = {
                Text(
                    text = stringResource(Res.string.app_bottom_nav_profile)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}
