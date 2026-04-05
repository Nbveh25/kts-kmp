package ru.kazan.itis.bikmukhametov.chat.impl.presentation.screen

import androidx.compose.runtime.Immutable
import ru.kazan.itis.bikmukhametov.chat.api.model.BlockModel
import ru.kazan.itis.bikmukhametov.chat.api.model.ChatMessageModel
import ru.kazan.itis.bikmukhametov.chat.api.model.ScenarioModel
import ru.kazan.itis.bikmukhametov.chat.impl.presentation.model.PickedAttachment

internal enum class RunScenarioDialogStep {
    ChooseScenario,
    ChooseBlock,
}

@Immutable
internal data class ChatUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val loadError: String? = null,
    val messageList: List<ChatMessageModel> = emptyList(),
    val messageText: String = "",
    val botRunning: Boolean? = null,
    val menuExpanded: Boolean = false,
    val runScenarioDialogVisible: Boolean = false,
    val runScenarioSearchQuery: String = "",
    val scenarios: List<ScenarioModel> = emptyList(),
    val scenariosLoading: Boolean = false,
    val scenariosLoadError: String? = null,
    val runScenarioStep: RunScenarioDialogStep = RunScenarioDialogStep.ChooseScenario,
    val runScenarioSelectedScenario: ScenarioModel? = null,
    val blocks: List<BlockModel> = emptyList(),
    val blocksLoading: Boolean = false,
    val blocksLoadError: String? = null,
    val runScenarioSelectedBlockId: String? = null,
    val interlocutorName: String? = null,
    val interlocutorAvatarUrl: String? = null,
    val channelKind: String? = null,
    val channelName: String? = null,
    /** Mongo `_id` канала для API (`chat_id`). */
    val channelMongoId: String? = null,
    /** Mongo `_id` пользователя для API (`user_id`). */
    val userMongoId: String? = null,

    val attachmentPickerVisible: Boolean = false,
    val pendingAttachment: PickedAttachment? = null,
    val isUploading: Boolean = false,
)
