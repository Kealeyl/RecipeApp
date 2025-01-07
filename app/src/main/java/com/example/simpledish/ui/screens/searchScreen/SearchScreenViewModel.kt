package com.example.simpledish.ui.screens.searchScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpledish.data.database.SimpleDishDaoImpl
import com.example.simpledish.model.Dish
import com.example.simpledish.navigation.HomeDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val databaseRepository: SimpleDishDaoImpl
) : ViewModel() {

    private val userId: Int = checkNotNull(savedStateHandle[HomeDestination.userIdArg])

    // Backing property
    private val _uiState = MutableStateFlow(
        SearchScreenUIState(
            userId = userId,
            listOfDishesSearch = emptyList(),
            listOfDishes = emptyList()
        )
    )

    init {
        reinitialize()
    }

    fun reinitialize() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    listOfDishes = databaseRepository.getAllDishes()
                )
            }
        }
    }

    // read-only state flow
    val uiState: StateFlow<SearchScreenUIState> = _uiState.asStateFlow()

    fun onSearch(search: String) {
        _uiState.update {
            it.copy(search = search,
                searched = if (search == "" && it.searched) false else it.searched)
        }
    }

    fun onNameButton() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    searched = true,
                    listOfDishesSearch = databaseRepository.getAllDishesByNameSearch(it.search))
            }
        }
    }

    fun onIngredientButton() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    searched = true,
                    listOfDishesSearch = databaseRepository.getAllDishesByIngredientSearch(it.search))
            }
        }
    }
}