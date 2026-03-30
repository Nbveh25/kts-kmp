package ru.kazan.itis.bikmukhametov.main.impl.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_filter_24
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.ic_search_24
import ru.kazan.itis.bikmukhametov.main.impl.generated.resources.search
import ru.kazan.itis.bikmukhametov.theme.Spacing
import ru.kazan.itis.bikmukhametov.ui.component.CabinetProjectTopBar
import ru.kazan.itis.bikmukhametov.ui.model.CabinetUi
import ru.kazan.itis.bikmukhametov.ui.model.ProjectUi
import androidx.compose.material3.Text

@Composable
internal fun ChatListTopBar(
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

    searchExpanded: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchToggle: () -> Unit,
    onFilterClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CabinetProjectTopBar(
            currentCabinet = currentCabinet,
            cabinets = cabinets,
            cabinetDropdownExpanded = cabinetDropdownExpanded,
            onCabinetDropdownChange = onCabinetDropdownChange,
            onCabinetSelect = onCabinetSelect,
            currentProject = currentProject,
            projects = projects,
            projectDropdownExpanded = projectDropdownExpanded,
            onProjectDropdownChange = onProjectDropdownChange,
            onProjectSelect = onProjectSelect,
            trailingContent = {
                IconButton(onClick = onSearchToggle) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_search_24),
                        contentDescription = "Поиск"
                    )
                }
            }
        )

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
                            text = stringResource(Res.string.search),
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
                        contentDescription = "Фильтр"
                    )
                }
            }
        }
    }
}
