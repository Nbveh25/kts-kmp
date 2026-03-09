package ru.kazan.itis.bikmukhametov.main.impl.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.vectorResource
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_filter_24
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_search_24
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.ProjectUi
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.SpaceUi
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun ChatListTopBar(
    currentSpace: SpaceUi?,
    spaces: List<SpaceUi>,
    spaceDropdownExpanded: Boolean,
    onSpaceDropdownChange: (Boolean) -> Unit,
    onSpaceSelect: (SpaceUi) -> Unit,
    currentProject: ProjectUi?,
    projects: List<ProjectUi>,
    projectDropdownExpanded: Boolean,
    onProjectDropdownChange: (Boolean) -> Unit,
    onProjectSelect: (ProjectUi) -> Unit,
    searchExpanded: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchToggle: () -> Unit,
    onFilterClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Surface(color = MaterialTheme.colorScheme.surface) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.paddingMedium, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.paddingSmall)
                ) {
                    Box {
                        Row(
                            modifier = Modifier
                                .clickable { onSpaceDropdownChange(!spaceDropdownExpanded) }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currentSpace?.displayName ?: "Пространство",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "▼",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        DropdownMenu(
                            expanded = spaceDropdownExpanded,
                            onDismissRequest = { onSpaceDropdownChange(false) }
                        ) {
                            spaces.forEach { space ->
                                DropdownMenuItem(
                                    text = { Text(space.displayName) },
                                    onClick = {
                                        onSpaceSelect(space)
                                        onSpaceDropdownChange(false)
                                    }
                                )
                            }
                        }
                    }

                    Box {
                        Row(
                            modifier = Modifier
                                .clickable { onProjectDropdownChange(!projectDropdownExpanded) }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currentProject?.displayName ?: "Проект",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "▼",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        DropdownMenu(
                            expanded = projectDropdownExpanded,
                            onDismissRequest = { onProjectDropdownChange(false) }
                        ) {
                            projects.forEach { project ->
                                DropdownMenuItem(
                                    text = { Text(project.displayName) },
                                    onClick = {
                                        onProjectSelect(project)
                                        onProjectDropdownChange(false)
                                    }
                                )
                            }
                        }
                    }
                }

                IconButton(onClick = onSearchToggle) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_search_24),
                        contentDescription = "Поиск",
                    )
                }
            }
        }

        AnimatedVisibility(visible = searchExpanded) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.paddingMedium, vertical = Spacing.paddingExtraSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    placeholder = {
                        Text(
                            text = "Поиск",
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
                IconButton(onClick = onFilterClick) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_filter_24),
                        contentDescription = "Фильтр",
                    )
                }
            }
        }
    }
}
