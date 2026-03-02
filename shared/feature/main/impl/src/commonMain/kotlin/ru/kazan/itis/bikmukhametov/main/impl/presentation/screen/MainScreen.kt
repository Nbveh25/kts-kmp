package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import ru.kazan.itis.bikmukhametov.main.impl.presentation.component.MainListItem
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
fun MainScreen() {
    val viewModel: MainViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = Spacing.horizontalScreenPadding,
                vertical = Spacing.verticalScreenPadding
            ),
        verticalArrangement = Arrangement.spacedBy(Spacing.paddingMedium)
    ) {
        items(
            items = state.items,
            key = { item -> item.id }
        ) { item ->
            MainListItem(ui = item)
        }
    }
}

