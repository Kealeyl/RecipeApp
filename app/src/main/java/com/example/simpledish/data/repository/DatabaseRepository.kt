package com.example.simpledish.data.repository

import com.example.simpledish.data.database.SimpleDishDAO
import com.example.simpledish.model.Dish
import com.example.simpledish.model.Ingredient

//// dependency injection
//class DatabaseRepository (val simpleDishDAO: SimpleDishDAO) : SimpleDishRepository {
//    override suspend fun getUserId(username: String, password: String): Int? {
//        return simpleDishDAO.getUserId(username, password)
//    }
//
//    override suspend fun insertUserDish(dish: Dish) {
//        simpleDishDAO.insertUserDish(dish)
//    }
//
//    override suspend fun updateUserDish(dish: Dish) {
//        simpleDishDAO.updateUserDish(dish)
//    }
//
//    override suspend fun deleteUserDish(dish: Dish) {
//        simpleDishDAO.deleteUserDish(dish)
//    }
//
//    override suspend fun getUserAddedDishes(userId: Int): List<Dish> {
//        return simpleDishDAO.getUserAddedDishes(userId)
//    }
//
//    // Saves
//    override suspend fun getUserBookMarks(): List<Dish> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun addUserBookMark(dishId: Int) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun deleteUserBookMark(dishId: Int) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getAllUserBookMarksBySearch(search: String): List<Dish> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getAllDishesBySearch(search: String): List<Dish> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getAllDishes(): List<Dish> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getDish(dishId: Int): Dish {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getIngredientsBySearch(search: String): List<Ingredient> {
//        TODO("Not yet implemented")
//    }
//}