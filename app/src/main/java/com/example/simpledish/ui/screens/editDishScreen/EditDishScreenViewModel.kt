package com.example.simpledish.ui.screens.editDishScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpledish.data.database.SimpleDishDaoImpl
import com.example.simpledish.model.Dish
import com.example.simpledish.model.Ingredient
import com.example.simpledish.model.PrepTime
import com.example.simpledish.navigation.EditDishDestination
import com.example.simpledish.navigation.HomeDestination
import com.example.simpledish.ui.screens.homeScreen.HomeScreenUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// fetch from DB, update dish

class EditDishScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val databaseRepository: SimpleDishDaoImpl
) : ViewModel() {

    private val dishId: Int = checkNotNull(savedStateHandle[EditDishDestination.dishIdArg])

    // Backing property
    private val _uiState = MutableStateFlow(EditScreenUIState())

    // read-only state flow
    val uiState: StateFlow<EditScreenUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {

            val dish = databaseRepository.getDish(dishId = dishId)!!
            val ingredients = databaseRepository.getIngredientsByDishId(dishId).toMutableList()


            while (ingredients.size < 5) {
                ingredients.add(Ingredient(ingredientId = 0, ingredientName = "", measurementUnit = "", quantity = ""))
            }

            _uiState.update {
                it.copy(
                    dish = dish,
                    ingredients = ingredients
                )
            }
        }
    }


    fun updateDatabase() { // update
        viewModelScope.launch {
            databaseRepository.updateFullDish(
                dish = _uiState.value.dish,
                ingredientList = _uiState.value.ingredients,
                dishId = dishId
            )
        }
    }

    fun updateUiState(dish: Dish) {
        _uiState.update {
            it.copy(dish = dish, isEntryValid = validateDish(dish))
        }
    }

    fun updateIngredientsState(ingredient: Ingredient, index: Int) {
        _uiState.update {
            it.copy(ingredients = it.ingredients.mapIndexed
            { i, ing ->
                if (i == index) {
                    ingredient
                } else {
                    ing
                }
            })
        }

        _uiState.update {
            it.copy(isEntryValid = validateDish(it.dish))
        }
    }

    private fun validateDish(dish: Dish): Boolean {
        with(dish) {
            if (name.isNotEmpty() && validatePrepTime(prepTime)) {

                var atLeastOneValidIngredient = false

                for (i in _uiState.value.ingredients.indices) {

                    if (validateAnIngredient(_uiState.value.ingredients[i])) {
                        atLeastOneValidIngredient = true
                    }
                }

                if (atLeastOneValidIngredient) {
                    return true
                }
            }
            return false
        }
    }

    private fun validateAnIngredient(ingredient: Ingredient): Boolean {
        return ingredient.ingredientName.isNotEmpty()
                && ingredient.quantity.isNotEmpty()
    }

    private fun validatePrepTime(prepTime: PrepTime): Boolean {
        return prepTime.hour.isNotEmpty() || prepTime.minute.isNotEmpty()
    }
}