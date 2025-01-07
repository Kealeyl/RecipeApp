package com.example.simpledish.ui.screens.searchScreen

import com.example.simpledish.model.Dish

data class SearchScreenUIState(
    val search: String = "",
    val userId: Int,
    val searched: Boolean = false,
    val listOfDishes: List<Dish> = listOf(),
    val listOfDishesSearch: List<Dish> = listOf()
)