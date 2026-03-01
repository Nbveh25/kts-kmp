package ru.kazan.itis.bikmukhametov.impl.presentation.screen

import androidx.lifecycle.ViewModel

/* Вьюмодель экрана входа */
class LoginViewModel: ViewModel() {
    override fun onCleared() {
        super.onCleared()
        println("LoginViewModel cleared")
    
    }
}