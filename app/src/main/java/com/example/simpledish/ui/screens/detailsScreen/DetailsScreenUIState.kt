package com.example.simpledish.ui.screens.detailsScreen

import com.example.simpledish.data.dummyData.dish2
import com.example.simpledish.model.Dish
import com.example.simpledish.model.Ingredient
import com.example.simpledish.model.PrepTime

data class DetailsScreenUIState(
    val didUserCreateDish: Boolean = false,
    val didUserSaveDish: Boolean = false,
    val userId: Int,
    val dishId: Int,
    val ingredients: List<Ingredient> = emptyList(),
    val dish: Dish = Dish(id = 0, name = "", servings = "", prepTime = PrepTime("", ""))
)