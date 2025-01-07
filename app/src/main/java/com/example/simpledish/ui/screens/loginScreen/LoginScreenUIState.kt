package com.example.simpledish.ui.screens.loginScreen

data class LoginScreenUIState(
    val username: String = "",
    val password: String = "",
    val isWrongUserNameOrPassword: Boolean = false,
    val isValidInput: Boolean = false
)