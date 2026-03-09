package ru.kazan.itis.bikmukhametov.main.impl.presentation.component

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.vectorResource
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_account_circle_24
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_chat_24

@Composable
internal fun MainBottomNav(
    chatsSelected: Boolean,
    onChatsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = chatsSelected,
            onClick = onChatsClick,
            icon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_chat_24),
                    contentDescription = "Чаты"
                )
            },
            label = { Text("Чаты") },
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
                    contentDescription = "Профиль"
                )
            },
            label = { Text("Профиль") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}
