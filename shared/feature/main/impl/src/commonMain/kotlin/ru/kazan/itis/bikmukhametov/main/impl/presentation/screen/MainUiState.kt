package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import androidx.compose.runtime.Immutable
import ru.kazan.itis.bikmukhametov.ui.model.ProjectUi
import ru.kazan.itis.bikmukhametov.ui.model.CabinetUi
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.ConversationCardItem

/** Вкладка под Topbar: Все обращения / Ждут ответа */
enum class ChatListTab {
    ALL,
    WAITING
}

@Immutable
internal data class MainUiState(
    val isLoading: Boolean = true,
    val loadError: String? = null,

    val currentCabinet: CabinetUi? = null,
    val cabinets: List<CabinetUi> = emptyList(),
    val cabinetDropdownExpanded: Boolean = false,

    val currentProject: ProjectUi? = null,
    val projects: List<ProjectUi> = emptyList(),
    val projectDropdownExpanded: Boolean = false,

    val searchExpanded: Boolean = false,
    val searchQuery: String = "",

    val filterSheetVisible: Boolean = false,

    val selectedTab: ChatListTab = ChatListTab.ALL,

    // Все загруженные чаты (без фильтрации по вкладке и поиску)
    val allChats: List<ConversationCardItem> = emptyList(),
    // Отфильтрованные чаты для отображения (по вкладке + поиску)
    val chats: List<ConversationCardItem> = emptyList(),

    // Флаг для нижнего лоадера при пагинации.
    val isLoadingMore: Boolean = false,

    // Флаг для pull-to-refresh индикатора.
    val isRefreshing: Boolean = false,
)
