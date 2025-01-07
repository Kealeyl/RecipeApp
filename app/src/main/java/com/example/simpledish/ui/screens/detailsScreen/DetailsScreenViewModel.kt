package com.example.simpledish.ui.screens.detailsScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpledish.data.database.SimpleDishDaoImpl
import com.example.simpledish.data.dummyData.dish
import com.example.simpledish.model.Dish
import com.example.simpledish.model.Ingredient
import com.example.simpledish.model.PrepTime
import com.example.simpledish.navigation.DetailsDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val databaseRepository: SimpleDishDaoImpl
) : ViewModel() {

    private val userId: Int = checkNotNull(savedStateHandle[DetailsDestination.userIdArg]) // SavedStateHandle acts like a map
    private val dishId: Int = checkNotNull(savedStateHandle[DetailsDestination.dishIdArg])

    // Backing property
    private val _uiState = MutableStateFlow(
        DetailsScreenUIState(userId = userId, dishId = dishId)
    )

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    dish = databaseRepository.getDish(dishId = dishId)!!,
                    ingredients = databaseRepository.getIngredientsByDishId(dishId),
                    didUserCreateDish = databaseRepository.didUserCreateDish(userId, dishId),
                    didUserSaveDish = databaseRepository.didUserSaveDish(userId, dishId),
                )
            }
        }
    }

    fun reinitialize() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    dish = databaseRepository.getDish(dishId = dishId)!!,
                    ingredients = databaseRepository.getIngredientsByDishId(dishId),
                )
            }
        }
    }

    // read-only state flow
    val uiState: StateFlow<DetailsScreenUIState> = _uiState.asStateFlow()

    fun onAddSaveButtonClick(){
        viewModelScope.launch {
            databaseRepository.addUserBookMark(userId, dishId)
        }
        _uiState.update {
            it.copy(didUserSaveDish = true)
        }

    }

    fun onDeleteSaveButtonClick(){
        viewModelScope.launch {
            databaseRepository.deleteUserBookMark(userId, dishId)
        }
        _uiState.update {
            it.copy(didUserSaveDish = false)
        }
    }

    fun onDeleteDishButtonClick(){
        viewModelScope.launch {
            databaseRepository.deleteUserDish(userId, dishId)
        }
        _uiState.update {
            it.copy(didUserCreateDish = false)
        }
    }
}