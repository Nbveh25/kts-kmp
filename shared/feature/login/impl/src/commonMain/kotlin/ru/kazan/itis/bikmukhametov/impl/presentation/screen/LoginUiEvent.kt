package ru.kazan.itis.bikmukhametov.impl.presentation.screen

/* События экрана входа */
sealed class LoginUiEvent {
    /* Успешная авторизация */
    object LoginSuccessEvent : LoginUiEvent()
}
