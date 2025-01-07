package com.example.simpledish.ui.screens.addDishScreen

import com.example.simpledish.data.dummyData.dish2
import com.example.simpledish.data.dummyData.ingredientList
import com.example.simpledish.model.Dish
import com.example.simpledish.model.Ingredient
import com.example.simpledish.model.PrepTime

data class AddScreenUIState(
    val dish: Dish = Dish(id = 0, name = "", servings = "", prepTime = PrepTime("", "")),

    val ingredients: List<Ingredient> = List(5) {
        Ingredient(ingredientId = 0, ingredientName = "", measurementUnit = "", quantity = "")
    },

    val userId: Int,

    val isEntryValid: Boolean = false
)