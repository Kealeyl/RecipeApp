package com.example.simpledish.data.dummyData

import com.example.simpledish.model.Dish
import com.example.simpledish.model.Ingredient
import com.example.simpledish.model.PrepTime

val ingredient1 = Ingredient(ingredientId = 1, ingredientName = "Chicken", measurementUnit = "grams", quantity = "100.0")
val ingredient2 = Ingredient(ingredientId = 2, ingredientName = "Cheese", measurementUnit = "grams", quantity = "50.0")
val ingredient3 = Ingredient(ingredientId = 3, ingredientName = "Tomato sauce", measurementUnit = "grams", quantity = "75.0")

val ingredientList: List<Ingredient> = listOf(
    ingredient1, ingredient2, ingredient3, ingredient1, ingredient2)

val dish = Dish(
    name = "Chicken parm",
//    ingredients = ingredientList,
    prepTime = PrepTime("1", "30"),
    servings = "1",
    id = 1
)

val dish2 = Dish(
    name = "Chicken parm2",
//    ingredients = ingredientList,
    prepTime = PrepTime("0", "30"),
    servings = "1",
    id = 2
)

val dish3 = Dish(
    name = "Chicken parm3",
//    ingredients = ingredientList,
    prepTime = PrepTime("1", "0"),
    servings = "1",
    id = 3
)

val dishList: List<Dish> = listOf(dish, dish2, dish3, dish.copy(id = 4), dish2.copy(id = 5), dish3.copy(id = 6))
