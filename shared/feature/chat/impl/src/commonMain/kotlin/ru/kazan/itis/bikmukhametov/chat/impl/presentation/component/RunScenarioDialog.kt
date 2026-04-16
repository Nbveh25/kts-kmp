package ru.kazan.itis.bikmukhametov.chat.impl.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ru.kazan.itis.bikmukhametov.chat.api.model.BlockModel
import ru.kazan.itis.bikmukhametov.chat.api.model.ScenarioModel
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.Res
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_run_scenario_back_to_scenarios
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_run_scenario_blocks_empty
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_run_scenario_dialog_close
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_run_scenario_dialog_empty
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_run_scenario_dialog_info
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_run_scenario_dialog_search_placeholder
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_run_scenario_dialog_section_scenario
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_run_scenario_dialog_select
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.chat_run_scenario_dialog_title
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_chevron_right_24
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_close_24
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_info_24
import ru.kazan.itis.bikmukhametov.chat.impl.generated.resources.ic_search_24
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen.RunScenarioDialogStep
import ru.kazan.itis.bikmukhametov.theme.KtsDialogBody
import ru.kazan.itis.bikmukhametov.theme.KtsDialogTitle
import ru.kazan.itis.bikmukhametov.theme.KtsFieldBorder
import ru.kazan.itis.bikmukhametov.theme.KtsIconMuted
import ru.kazan.itis.bikmukhametov.theme.KtsInfoBackground
import ru.kazan.itis.bikmukhametov.theme.KtsInfoBorder
import ru.kazan.itis.bikmukhametov.theme.KtsPlaceholder
import ru.kazan.itis.bikmukhametov.theme.Spacing

@Composable
internal fun RunScenarioDialog(
    step: RunScenarioDialogStep,
    scenarios: List<ScenarioModel>,
    scenariosLoading: Boolean,
    scenariosLoadError: String?,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedScenario: ScenarioModel?,
    blocks: List<BlockModel>,
    blocksLoading: Boolean,
    blocksLoadError: String?,
    selectedBlockId: String?,
    onScenarioClick: (ScenarioModel) -> Unit,
    onBackToScenarios: () -> Unit,
    onBlockClick: (String) -> Unit,
    onDismiss: () -> Unit,
    onSelectClick: () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme

    val filteredScenarios = remember(searchQuery, scenarios) {
        val q = searchQuery.trim()
        if (q.isEmpty()) scenarios
        else scenarios.filter { it.name.contains(q, ignoreCase = true) }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .widthIn(max = 440.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = colorScheme.surface,
            shadowElevation = 8.dp,
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = Spacing.paddingMedium,
                    vertical = Spacing.paddingSmall,
                ),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(Res.string.chat_run_scenario_dialog_title),
                        modifier = Modifier.weight(1f),
                        color = KtsDialogTitle,
                        fontSize = 17.sp,
                        lineHeight = 22.sp,
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.ic_close_24),
                            contentDescription = stringResource(Res.string.chat_run_scenario_dialog_close),
                            tint = KtsPlaceholder,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                InfoBanner(text = stringResource(Res.string.chat_run_scenario_dialog_info))

                when (step) {
                    RunScenarioDialogStep.ChooseScenario -> {
                        ScenarioPickerBody(
                            colorScheme = colorScheme,
                            filteredScenarios = filteredScenarios,
                            scenariosLoading = scenariosLoading,
                            scenariosLoadError = scenariosLoadError,
                            searchQuery = searchQuery,
                            onSearchQueryChange = onSearchQueryChange,
                            onScenarioClick = onScenarioClick,
                        )
                    }

                    RunScenarioDialogStep.ChooseBlock -> {
                        val scenario = selectedScenario
                        if (scenario != null) {
                            BlockPickerBody(
                                colorScheme = colorScheme,
                                scenarioName = scenario.name,
                                blocks = blocks,
                                blocksLoading = blocksLoading,
                                blocksLoadError = blocksLoadError,
                                selectedBlockId = selectedBlockId,
                                onBackToScenarios = onBackToScenarios,
                                onBlockClick = onBlockClick,
                                footerScenarioName = scenario.name,
                                onSelectClick = onSelectClick,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScenarioPickerBody(
    colorScheme: ColorScheme,
    filteredScenarios: List<ScenarioModel>,
    scenariosLoading: Boolean,
    scenariosLoadError: String?,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onScenarioClick: (ScenarioModel) -> Unit,
) {
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = stringResource(Res.string.chat_run_scenario_dialog_section_scenario),
        modifier = Modifier.fillMaxWidth(),
        color = KtsDialogBody,
        fontSize = 15.sp,
        textAlign = TextAlign.Center,
    )

    HorizontalDivider(
        modifier = Modifier.padding(top = 8.dp),
        color = colorScheme.outlineVariant,
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(28.dp),
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholder = {
            Text(
                text = stringResource(Res.string.chat_run_scenario_dialog_search_placeholder),
                color = KtsPlaceholder,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_search_24),
                contentDescription = null,
                tint = KtsPlaceholder,
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = KtsFieldBorder,
            unfocusedBorderColor = KtsFieldBorder,
            focusedContainerColor = colorScheme.surface,
            unfocusedContainerColor = colorScheme.surface,
            cursorColor = KtsDialogBody,
            focusedTextColor = KtsDialogBody,
            unfocusedTextColor = KtsDialogBody,
        ),
    )

    Spacer(modifier = Modifier.height(8.dp))

    when {
        scenariosLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = colorScheme.primary,
                    strokeWidth = 3.dp,
                )
            }
        }

        scenariosLoadError != null -> {
            Text(
                text = scenariosLoadError,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }

        filteredScenarios.isEmpty() -> {
            Text(
                text = stringResource(Res.string.chat_run_scenario_dialog_empty),
                color = KtsDialogBody,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 12.dp),
            )
        }

        else -> {
            filteredScenarios.forEach { scenario ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onScenarioClick(scenario) }
                        .padding(vertical = 12.dp, horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = scenario.name,
                        color = KtsDialogBody,
                        fontSize = 16.sp,
                    )
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_chevron_right_24),
                        contentDescription = null,
                        tint = KtsIconMuted,
                    )
                }
            }
        }
    }
}

@Composable
private fun BlockPickerBody(
    colorScheme: ColorScheme,
    scenarioName: String,
    blocks: List<BlockModel>,
    blocksLoading: Boolean,
    blocksLoadError: String?,
    selectedBlockId: String?,
    onBackToScenarios: () -> Unit,
    onBlockClick: (String) -> Unit,
    footerScenarioName: String,
    onSelectClick: () -> Unit,
) {
    Spacer(modifier = Modifier.height(12.dp))

    TextButton(
        onClick = onBackToScenarios,
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "< " + stringResource(Res.string.chat_run_scenario_back_to_scenarios),
            color = colorScheme.primary,
            fontSize = 15.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
        )
    }

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = scenarioName,
        modifier = Modifier.fillMaxWidth(),
        color = KtsDialogBody,
        fontSize = 16.sp,
    )

    HorizontalDivider(
        modifier = Modifier.padding(top = 12.dp),
        color = colorScheme.outlineVariant,
    )

    Spacer(modifier = Modifier.height(16.dp))

    when {
        blocksLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = colorScheme.primary,
                    strokeWidth = 3.dp,
                )
            }
        }

        blocksLoadError != null -> {
            Text(
                text = blocksLoadError,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }

        blocks.isEmpty() -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BlocksEmptyIllustration()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.chat_run_scenario_blocks_empty),
                    color = KtsDialogBody,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }

        else -> {
            blocks.forEach { block ->
                val selected = block.id == selectedBlockId
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (selected) KtsInfoBackground.copy(alpha = 0.65f) else Color.Transparent,
                        )
                        .clickable { onBlockClick(block.id) }
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = block.name,
                        color = KtsDialogBody,
                        fontSize = 16.sp,
                    )
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_chevron_right_24),
                        contentDescription = null,
                        tint = KtsIconMuted,
                    )
                }
            }
        }
    }

    HorizontalDivider(
        modifier = Modifier.padding(top = 12.dp),
        color = colorScheme.outlineVariant,
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = footerScenarioName,
            color = KtsDialogTitle,
            fontSize = 15.sp,
            maxLines = 1,
            modifier = Modifier.weight(1f).padding(end = 12.dp),
        )
        Button(
            onClick = onSelectClick,
            enabled = selectedBlockId != null,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary,
                disabledContainerColor = colorScheme.primary.copy(alpha = 0.38f),
                disabledContentColor = colorScheme.onPrimary.copy(alpha = 0.6f),
            ),
        ) {
            Text(stringResource(Res.string.chat_run_scenario_dialog_select))
        }
    }
}

@Composable
private fun BlocksEmptyIllustration() {
    Box(
        modifier = Modifier
            .size(112.dp)
            .clip(CircleShape)
            .background(Color(0xFFB8D9F0).copy(alpha = 0.45f)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            repeat(3) {
                Box(
                    Modifier
                        .width(48.dp)
                        .height(10.dp)
                        .background(Color.White, RoundedCornerShape(3.dp))
                )
            }
        }
    }
}

@Composable
private fun InfoBanner(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, KtsInfoBorder, RoundedCornerShape(8.dp))
            .background(KtsInfoBackground, RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            imageVector = vectorResource(Res.drawable.ic_info_24),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 2.dp, end = 10.dp)
                .size(22.dp),
            tint = KtsDialogBody,
        )
        Text(
            text = text,
            color = KtsDialogBody,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            modifier = Modifier.weight(1f),
        )
    }
}
