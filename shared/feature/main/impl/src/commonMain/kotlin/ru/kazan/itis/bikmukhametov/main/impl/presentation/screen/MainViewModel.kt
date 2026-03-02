package ru.kazan.itis.bikmukhametov.main.impl.presentation.screen

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kazan.itis.bikmukhametov.main.api.usecase.GetPostListUseCase
import ru.kazan.itis.bikmukhametov.main.impl.presentation.model.MainItemUi
import ru.kazan.itis.bikmukhametov.ui.util.BasicViewModel

internal class MainViewModel(
    private val getPostListUseCase: GetPostListUseCase
) : BasicViewModel<MainUiState, Unit>(MainUiState(emptyList())) {

    init {
        viewModelScope.launch {
            val posts = getPostListUseCase().map { postModel ->
                MainItemUi(
                    id = postModel.id,
                    title = postModel.title,
                    subtitle = postModel.subtitle,
                    description = postModel.description,
                    imageUrl = postModel.imageUrl
                )
            }

            updateState {
                copy(items = posts)
            }
        }
    }

    override fun onAction(action: Unit) {
        // потом тут будут ингтенты
    }


}

