package com.example.simpledish.data.database

import com.example.simpledish.model.Dish
import com.example.simpledish.model.Ingredient


interface SimpleDishDAO {

    suspend fun getUserId(username: String, password: String): Int?

    suspend fun getUserAddedDishes(userId: Int): List<Dish>

    suspend fun getUserBookMarks(userId: Int): List<Dish>

    suspend fun getAllDishesByNameSearch(search: String): List<Dish>

    suspend fun getDish(dishId: Int): Dish?

    suspend fun getAllDishesByIngredientSearch(search: String): List<Dish>

    suspend fun getAllDishes(): List<Dish>

    suspend fun didUserCreateDish(userId: Int, dishId: Int): Boolean

    suspend fun didUserSaveDish(userId: Int, dishId: Int): Boolean

    suspend fun getIngredientsByDishId(dishId: Int): List<Ingredient>

    suspend fun addUserBookMark(userId: Int, dishId: Int)

    suspend fun deleteUserDish(userId: Int, dishId: Int)

    suspend fun deleteUserBookMark(userId: Int, dishId: Int)

    suspend fun insertFullDish(dish: Dish, ingredientList: List<Ingredient>, userId: Int)

    suspend fun updateFullDish(dish: Dish, ingredientList: List<Ingredient>, dishId: Int)
}