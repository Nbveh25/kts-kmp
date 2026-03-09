package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import androidx.compose.runtime.Immutable
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.ChatCardUi
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.ProjectUi
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.SpaceUi

/** Вкладка под Topbar: Все обращения / Ждут ответа */
enum class ChatListTab {
    ALL,
    WAITING
}

@Immutable
internal data class MainUiState(
    val currentSpace: SpaceUi? = null,
    val spaces: List<SpaceUi> = emptyList(),
    val spaceDropdownExpanded: Boolean = false,

    val currentProject: ProjectUi? = null,
    val projects: List<ProjectUi> = emptyList(),
    val projectDropdownExpanded: Boolean = false,

    val searchExpanded: Boolean = false,
    val searchQuery: String = "",

    val filterSheetVisible: Boolean = false,

    val selectedTab: ChatListTab = ChatListTab.ALL,

    val chats: List<ChatCardUi> = emptyList()
)
