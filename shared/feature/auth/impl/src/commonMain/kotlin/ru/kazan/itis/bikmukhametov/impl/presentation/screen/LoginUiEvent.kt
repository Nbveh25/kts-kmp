package ru.kazan.itis.bikmukhametov.impl.presentation.screen

/* События экрана входа */
internal sealed class LoginUiEvent {
    /* Успешная авторизация */
    object LoginSuccessEvent : LoginUiEvent()
}
