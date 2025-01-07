package com.example.simpledish.ui.screens.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpledish.data.database.SimpleDishDaoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginScreenViewModel(private val databaseRepository: SimpleDishDaoImpl) : ViewModel() {
    // Backing property
    private val _uiState = MutableStateFlow(LoginScreenUIState())

    // read-only state flow
    val uiState: StateFlow<LoginScreenUIState> = _uiState.asStateFlow()

    fun updateUiState(loginScreenUIState: LoginScreenUIState) {
        _uiState.update {
            it.copy(
                username = loginScreenUIState.username,
                password = loginScreenUIState.password,
                isValidInput = validateInput()
            )
        }
    }

    private fun validateInput(): Boolean {
        return _uiState.value.username.isNotEmpty() && _uiState.value.password.isNotEmpty()
    }

    // could return null
    fun getUserId(callback: (Int?) -> Unit)  {

        viewModelScope.launch {
            val userId = databaseRepository.getUserId(_uiState.value.username, _uiState.value.password)

            _uiState.update {
                it.copy(isWrongUserNameOrPassword = userId == null)
            }

            callback(userId) // Pass the result to the callback
        }
    }
}