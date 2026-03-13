package ru.kazan.itis.bikmukhametov.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import ru.kazan.itis.bikmukhametov.theme.Spacing
import ru.kazan.itis.bikmukhametov.ui.generated.resources.Res
import ru.kazan.itis.bikmukhametov.ui.generated.resources.top_bar_project
import ru.kazan.itis.bikmukhametov.ui.generated.resources.top_bar_space
import ru.kazan.itis.bikmukhametov.ui.model.CabinetUi
import ru.kazan.itis.bikmukhametov.ui.model.ProjectUi

@Composable
fun CabinetProjectTopBar(
    currentCabinet: CabinetUi?,
    cabinets: List<CabinetUi>,
    cabinetDropdownExpanded: Boolean,
    onCabinetDropdownChange: (Boolean) -> Unit,
    onCabinetSelect: (CabinetUi) -> Unit,

    currentProject: ProjectUi?,
    projects: List<ProjectUi>,
    projectDropdownExpanded: Boolean,
    onProjectDropdownChange: (Boolean) -> Unit,
    onProjectSelect: (ProjectUi) -> Unit,

    modifier: Modifier = Modifier,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
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
                            .clickable { onCabinetDropdownChange(!cabinetDropdownExpanded) }
                            .heightIn(min = 48.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentCabinet?.displayName ?: stringResource(Res.string.top_bar_space),
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
                        expanded = cabinetDropdownExpanded,
                        onDismissRequest = { onCabinetDropdownChange(false) }
                    ) {
                        cabinets.forEach { cabinet ->
                            DropdownMenuItem(
                                text = { Text(cabinet.displayName) },
                                onClick = {
                                    onCabinetSelect(cabinet)
                                    onCabinetDropdownChange(false)
                                }
                            )
                        }
                    }
                }

                Box {
                    Row(
                        modifier = Modifier
                            .clickable { onProjectDropdownChange(!projectDropdownExpanded) }
                            .heightIn(min = 48.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentProject?.displayName ?: stringResource(Res.string.top_bar_project),
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

            if (trailingContent != null) {
                trailingContent()
            }
        }
    }
}
