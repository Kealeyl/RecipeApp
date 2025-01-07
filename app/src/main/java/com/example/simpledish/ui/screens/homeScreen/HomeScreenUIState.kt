package com.example.simpledish.ui.screens.homeScreen

import com.example.simpledish.model.Dish

data class HomeScreenUIState(
    val searchAdded: String = "",
    val searchSaved: String = "",
    val userId: Int,
    val userAddedDishList: List<Dish> = emptyList(),
    val userSavedDishList: List<Dish> = emptyList()
)