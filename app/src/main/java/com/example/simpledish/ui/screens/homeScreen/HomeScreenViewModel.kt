package com.example.simpledish.ui.screens.homeScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpledish.data.database.SimpleDishDaoImpl
import com.example.simpledish.navigation.HomeDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// search

class HomeScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val databaseRepository: SimpleDishDaoImpl
) : ViewModel() {

    private val userId: Int = checkNotNull(savedStateHandle[HomeDestination.userIdArg])

    // Backing property
    private val _uiState = MutableStateFlow(
        HomeScreenUIState(
            userId = userId,
            userAddedDishList = emptyList(),
            userSavedDishList = emptyList()
        )
    )

    // read-only state flow
    val uiState: StateFlow<HomeScreenUIState> = _uiState.asStateFlow()

    init {
        Log.d("HomeViewModel", "In home view model init")
        reinitialize()
    }

    fun reinitialize() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    userAddedDishList = databaseRepository.getUserAddedDishes(it.userId),
                    userSavedDishList = databaseRepository.getUserBookMarks(it.userId)
                )
            }
        }
    }

    fun onSearchAdded(search: String) {
        _uiState.update {
            it.copy(searchAdded = search)
        }
    }

    fun onSearchSaved(search: String) {
        _uiState.update {
            it.copy(searchSaved = search)
        }
    }
}