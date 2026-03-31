package ru.kazan.itis.bikmukhametov.main.impl.presentation.component.filter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun FilterAccordionSection(
    label: String,
    summary: String,
    enabled: Boolean,
    content: @Composable () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val fieldTapInteraction = remember { MutableInteractionSource() }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = summary,
                onValueChange = {},
                readOnly = true,
                enabled = enabled,
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Text(
                        text = "▼",
                        modifier = Modifier.rotate(if (expanded) 180f else 0f),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                ),
                shape = MaterialTheme.shapes.medium,
            )
            if (enabled) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable(
                            interactionSource = fieldTapInteraction,
                            indication = null,
                        ) { expanded = !expanded },
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
