package ru.kazan.itis.bikmukhametov.main.impl.presentation.component.filter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.vectorResource
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_arrow_drop_down_24
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_arrow_drop_up_24
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_search_24
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun FilterAccordionSection(
    label: String,
    summary: String,
    enabled: Boolean,
    searchQuery: String = "",
    searchPlaceholder: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    searchEnabled: Boolean = false,
    onExpandedChange: (Boolean) -> Unit = {},
    content: @Composable () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val fieldTapInteraction = remember { MutableInteractionSource() }
    val isSearchMode = expanded && enabled && searchEnabled

    fun toggleExpanded() {
        expanded = !expanded
        onExpandedChange(expanded)
    }

    LaunchedEffect(enabled) {
        if (!enabled && expanded) {
            expanded = false
            onExpandedChange(false)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = if (isSearchMode) searchQuery else summary,
                onValueChange = { if (isSearchMode) onSearchQueryChange(it) },
                readOnly = !isSearchMode,
                enabled = enabled,
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = if (isSearchMode) {
                    {
                        Icon(
                            imageVector = vectorResource(Res.drawable.ic_search_24),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                } else {
                    null
                },
                placeholder = if (isSearchMode && searchPlaceholder.isNotBlank()) {
                    {
                        Text(
                            text = searchPlaceholder,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
                    null
                },
                trailingIcon = {
                    IconButton(onClick = { toggleExpanded() }) {
                        Icon(
                            imageVector = if (expanded) vectorResource(Res.drawable.ic_arrow_drop_up_24)
                            else vectorResource(Res.drawable.ic_arrow_drop_down_24),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                ),
                shape = MaterialTheme.shapes.medium,
            )
            if (enabled && !isSearchMode) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable(
                            interactionSource = fieldTapInteraction,
                            indication = null,
                        ) { toggleExpanded() },
                )
            }
        }
        AnimatedVisibility(visible = expanded && enabled) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.paddingSmall),
            ) {
                content()
            }
        }
    }
}
