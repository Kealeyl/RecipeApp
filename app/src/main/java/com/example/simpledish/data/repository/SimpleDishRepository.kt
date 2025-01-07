package com.example.simpledish.data.repository

import com.example.simpledish.model.Dish
import com.example.simpledish.model.Ingredient

//// if ever had multiple data sources
//interface SimpleDishRepository {
//
//    suspend fun getUserId(username: String, password: String): Int?
//
//    suspend fun insertUserDish(dish: Dish)
//
//    suspend fun updateUserDish(dish: Dish)
//
//    suspend fun deleteUserDish(dish: Dish)
//
//    suspend fun getUserAddedDishes(userId: Int): List<Dish>
//
//    suspend fun getUserBookMarks(): List<Dish>
//
//    suspend fun addUserBookMark(dishId: Int)
//
//    suspend fun deleteUserBookMark(dishId: Int)
//
//    suspend fun getAllUserBookMarksBySearch(search: String): List<Dish>
//
//    suspend fun getAllDishesBySearch(search: String): List<Dish>
//
//    suspend fun getAllDishes(): List<Dish>
//
//    suspend fun getDish(dishId: Int): Dish?
//
//    suspend fun getIngredientsBySearch(search: String): List<Ingredient>
//}