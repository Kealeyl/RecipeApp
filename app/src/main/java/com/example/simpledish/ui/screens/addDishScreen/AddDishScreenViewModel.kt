package com.example.simpledish.ui.screens.addDishScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpledish.data.database.SimpleDishDaoImpl
import com.example.simpledish.model.Dish
import com.example.simpledish.model.Ingredient
import com.example.simpledish.model.PrepTime
import com.example.simpledish.navigation.HomeDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// add button to add to the database

class AddDishScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val databaseRepository: SimpleDishDaoImpl
) : ViewModel() {

    private val userId: Int = checkNotNull(savedStateHandle[HomeDestination.userIdArg])

    // Backing property
    private val _uiState = MutableStateFlow(AddScreenUIState(userId = userId))

    // read-only state flow
    val uiState: StateFlow<AddScreenUIState> = _uiState.asStateFlow()

    fun addToDatabase() {
        viewModelScope.launch {
            databaseRepository.insertFullDish(
                dish = _uiState.value.dish,
                ingredientList = _uiState.value.ingredients,
                userId = userId
            )
        }
    }

    fun resetDishAndIngredients(){
        val dish = Dish(id = 0, name = "", servings = "", prepTime = PrepTime("", ""))

        val ingredients: List<Ingredient> = List(5) {
            Ingredient(ingredientId = 0, ingredientName = "", measurementUnit = "", quantity = "")
        }

        _uiState.update {
            it.copy(dish = dish, isEntryValid = false, ingredients = ingredients)
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
        return ingredient.ingredientName.isNotEmpty() && ingredient.quantity.isNotEmpty()
    }

    private fun validatePrepTime(prepTime: PrepTime): Boolean {
        return prepTime.hour.isNotEmpty() || prepTime.minute.isNotEmpty()
    }
}