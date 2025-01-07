package com.example.simpledish.ui.screens.editDishScreen

import com.example.simpledish.data.dummyData.dish2
import com.example.simpledish.model.Dish
import com.example.simpledish.model.Ingredient
import com.example.simpledish.model.PrepTime

data class EditScreenUIState(
    val dish: Dish = Dish(id = 0, name = "", servings = "", prepTime = PrepTime("", "")),

    val ingredients: List<Ingredient> = List(5) {
        Ingredient(ingredientId =0,ingredientName = "", measurementUnit = "", quantity = "")
    },

    val isEntryValid: Boolean = true
)