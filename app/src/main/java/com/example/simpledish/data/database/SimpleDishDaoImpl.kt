package com.example.simpledish.data.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.simpledish.model.Dish
import com.example.simpledish.model.Ingredient
import com.example.simpledish.model.PrepTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SimpleDishDaoImpl(private val db: SQLiteDatabase) : SimpleDishDAO {

    // execution is off the main thread with withContext(Dispatchers.IO)

    //    SELECT ingredient_name, measurement_unit, quantity FROM ingredients
//    JOIN dish_ingredients ON ingredients.ingredient_id = dish_ingredients.ingredient_id
//    JOIN dishes ON dish_ingredients.dish_id = dishes.dish_id WHERE dishes.dish_id like 1;

    // link ingredients to dish_ingredients
    // link dish_ingredients to dishes
    override suspend fun getIngredientsByDishId(dishId: Int): List<Ingredient> =
        withContext(Dispatchers.IO) {

            val query = "SELECT ingredients.ingredient_id, ingredient_name, measurement_unit, quantity FROM ingredients " +
                    "JOIN dish_ingredients ON ingredients.ingredient_id = dish_ingredients.ingredient_id " +
                    "JOIN dishes ON dish_ingredients.dish_id = dishes.dish_id WHERE dishes.dish_id like ?"

            val cursor = db.rawQuery(query, arrayOf(dishId.toString()))
            val ingredients = mutableListOf<Ingredient>()

            // Iterate through the cursor
            if (cursor.moveToFirst()) {
                do {
                    val ingredient = Ingredient(
                        ingredientId = cursor.getInt(cursor.getColumnIndexOrThrow("ingredient_id")),
                        ingredientName = cursor.getString(cursor.getColumnIndexOrThrow("ingredient_name")),
                        measurementUnit = cursor.getString(cursor.getColumnIndexOrThrow("measurement_unit")),
                        quantity = cursor.getDouble(cursor.getColumnIndexOrThrow("quantity"))
                            .toString()
                    )
                    ingredients.add(ingredient)
                } while (cursor.moveToNext())
            }

            cursor.close()
            return@withContext ingredients
        }

    //    SELECT dishes.dish_id, dishes.dish_name, dishes.servings, dishes.prep_time_hours, dishes.prep_time_minutes
//    FROM dishes
//    JOIN dish_ingredients ON dishes.dish_id = dish_ingredients.dish_id
//    JOIN ingredients ON dish_ingredients.ingredient_id = ingredients.ingredient_id
//    WHERE ingredients.ingredient_name = 'Tomato';

    // find out the many dishes that have specific ingredients
    // link dishes to dish_ingredients
    // link dish_ingredients to ingredients
    override suspend fun getAllDishesByIngredientSearch(search: String): List<Dish> =
        withContext(Dispatchers.IO) {
            val query =
                "SELECT dishes.dish_id, dishes.dish_name, dishes.servings, dishes.prep_time_hours, dishes.prep_time_minutes FROM dishes " +
                        "JOIN dish_ingredients ON dishes.dish_id = dish_ingredients.dish_id " +
                        "JOIN ingredients ON dish_ingredients.ingredient_id = ingredients.ingredient_id WHERE ingredients.ingredient_name like ?"
            val cursor = db.rawQuery(query, arrayOf("%$search%"))
            val dishes = mutableListOf<Dish>()

            // Iterate through the cursor
            if (cursor.moveToFirst()) {
                do {
                    val dish = Dish(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("dish_id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("dish_name")),
                        servings = cursor.getInt(cursor.getColumnIndexOrThrow("servings"))
                            .toString(),
                        prepTime = PrepTime(
                            hour = cursor.getInt(cursor.getColumnIndexOrThrow("prep_time_hours"))
                                .toString(),
                            minute = cursor.getInt(cursor.getColumnIndexOrThrow("prep_time_minutes"))
                                .toString()
                        )
                    )
                    dishes.add(dish)

                } while (cursor.moveToNext())
            } else {
                cursor.close()
                return@withContext emptyList()
            }

            cursor.close()
            return@withContext dishes
        }



    override suspend fun getUserId(username: String, password: String): Int? =
        withContext(Dispatchers.IO) {

            // SELECT user_id from users WHERE username = 'user1' AND password = 'password1';

            val query = "SELECT user_id from users WHERE username = ? AND password = ?"
            val cursor = db.rawQuery(query, arrayOf(username, password))
            var userId: Int? = null

            // Iterate through the cursor
            if (cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"))
            }

            cursor.close()
            Log.d("getUserId", userId.toString())
            return@withContext userId // returns null if no user found
        }

    override suspend fun insertFullDish(dish: Dish, ingredientList: List<Ingredient>, userId: Int) =
        withContext(Dispatchers.IO) {
            val dishId = insertOnlyDish(dish, userId)
            val listOfIngredientIds: MutableList<Int> = mutableListOf()

            for (i in ingredientList.indices) {

                if(ingredientList[i].ingredientName.isEmpty()){
                    continue
                }

                listOfIngredientIds.add(insertOneIngredient(ingredientList[i].ingredientName))
            }

            for (i in ingredientList.indices) {

                if(ingredientList[i].ingredientName.isEmpty()){
                    continue
                }

                insertOneIngredientForDish(
                    dishId = dishId,
                    ingredientId = listOfIngredientIds[i],
                    measurementUnit = ingredientList[i].measurementUnit,
                    quantity = ingredientList[i].quantity
                )

                Log.d("addToDB insertFullDish", ingredientList[i].quantity)

                Log.d("insertFullDish", ingredientList.size.toString())
            }
        }

    override suspend fun updateFullDish(dish: Dish, ingredientList: List<Ingredient>, dishId: Int) =
        withContext(Dispatchers.IO) {

            updateOnlyDish(dishId = dishId, dish = dish)

            val listOfIngredientIds: MutableList<Int> = mutableListOf()

            for (i in ingredientList.indices) {

                if(ingredientList[i].ingredientName.isEmpty()){
                    continue
                }

                Log.d(
                    "updateFull", ingredientList[i].ingredientName
                )

                // select ingredient_id from dish_ingredients join ingredients on dish_ingredients.ingredient_id = ingredients.ingredient_id;

                var oldIngId: Int? = null
                val queryForOldIngID = "select ingredients.ingredient_id from dish_ingredients join ingredients " +
                        "on dish_ingredients.ingredient_id = ingredients.ingredient_id where ingredient_name = ? AND dish_id = ?"
                val cursor = db.rawQuery(queryForOldIngID, arrayOf(ingredientList[i].ingredientName, dishId.toString()))

                if (cursor.moveToFirst()) {
                    oldIngId = cursor.getInt(cursor.getColumnIndexOrThrow("ingredient_id"))
                }
                cursor.close()

                // null delete old
                if(oldIngId != null){
                    listOfIngredientIds.add(oldIngId)


                    updateOneIngredientForDish(
                        dishId = dishId,
                        ingredientId = listOfIngredientIds[i],
                        measurementUnit = ingredientList[i].measurementUnit,
                        quantity = ingredientList[i].quantity
                    )
                } else {
                    // delete because you have to change primary key
                    val query = "DELETE FROM dish_ingredients WHERE ingredient_id = ? AND dish_id = ?"
                    db.execSQL(query, arrayOf(ingredientList[i].ingredientId.toString(), dishId.toString()))

                    listOfIngredientIds.add(insertOneIngredient(ingredientList[i].ingredientName))

                    insertOneIngredientForDish(
                        dishId = dishId,
                        ingredientId = listOfIngredientIds[i],
                        measurementUnit = ingredientList[i].measurementUnit,
                        quantity = ingredientList[i].quantity
                    )
                }
            }
        }

    private suspend fun updateOnlyDish(dish: Dish, dishId: Int) = withContext(Dispatchers.IO) {

        // update dishes set servings = 0, dish_name = 'hi', prep_time_minutes = 9, prep_time_hours = 33 where (servings != 0 OR dish_name != 'hi' OR prep_time_minutes != 9 OR prep_time_hours != 33) AND dish_id = 1;

        val query =
            "update dishes set servings = ?, dish_name = ?, prep_time_minutes = ?, prep_time_hours = ? " +
                    "where (servings != ? OR dish_name != ? OR prep_time_minutes != ? OR prep_time_hours != ?)  AND dish_id = ?"
        db.execSQL(
            query,
            arrayOf(
                if(dish.servings.isEmpty()) null else dish.servings.toInt(),
                dish.name,
                if(dish.prepTime.minute.isEmpty()) null else dish.prepTime.minute.toInt(),
                if(dish.prepTime.hour.isEmpty()) null else dish.prepTime.hour.toInt(),
                if(dish.servings.isEmpty()) null else dish.servings.toInt(),
                dish.name,
                if(dish.prepTime.minute.isEmpty()) null else dish.prepTime.minute.toInt(),
                if(dish.prepTime.hour.isEmpty()) null else dish.prepTime.hour.toInt(),
                dishId.toString()
            )
        )
    }

    private suspend fun insertOnlyDish(dish: Dish, userId: Int): Int = withContext(Dispatchers.IO) {

        // INSERT INTO dishes (user_id, servings, dish_name, prep_time_minutes, prep_time_hours) VALUES(2, 7, "Soup milk", 35, 1);

        var dishId = 0

        val query = "INSERT INTO dishes (user_id, servings, dish_name, prep_time_minutes, prep_time_hours) VALUES(?, ?, ?, ?, ?)"
        db.execSQL(
            query,
            arrayOf(
                userId.toString(),
                if(dish.servings.isEmpty()) null else dish.servings.toInt(),
                dish.name,
                if(dish.prepTime.minute.isEmpty()) null else dish.prepTime.minute.toInt(),
                if(dish.prepTime.hour.isEmpty()) null else dish.prepTime.hour.toInt()
            )
        )

        val queryForID = "select dish_id from dishes where dish_name = ?"
        val cursor = db.rawQuery(queryForID, arrayOf(dish.name))

        if (cursor.moveToFirst()) {
            dishId = cursor.getInt(cursor.getColumnIndexOrThrow("dish_id"))
        }

        cursor.close()
        return@withContext dishId

        // insert each ingredient into dish_ingredients
    }

    // INSERT OR IGNORE INTO ingredients (ingredient_name) VALUES ('apple');
    // select ingredient_id from ingredients where ingredient_name = 'apple';
    // return ingredient id
    private suspend fun insertOneIngredient(ingredientName: String): Int =
        withContext(Dispatchers.IO) {
            var ingredientId = 0

            val query = "INSERT OR IGNORE INTO ingredients (ingredient_name) VALUES (?)"
            db.execSQL(query, arrayOf(ingredientName))

            val queryForID = "select ingredient_id from ingredients where ingredient_name = ?"
            val cursor = db.rawQuery(queryForID, arrayOf(ingredientName))
            if (cursor.moveToFirst()) {
                ingredientId = cursor.getInt(cursor.getColumnIndexOrThrow("ingredient_id"))
            }
            cursor.close()
            return@withContext ingredientId
        }

    // update dish_ingredients set quantity = 0, measurement_unit = 0 WHERE (quantity != 0 OR measurement_unit != 0) AND dish_id = 1 AND ingredient_id = 1;
    private suspend fun updateOneIngredientForDish(
        quantity: String, //
        measurementUnit: String,
        ingredientId: Int,
        dishId: Int
    ) = withContext(Dispatchers.IO) {

        val query =
            "update dish_ingredients set quantity = ?, measurement_unit = ? WHERE (quantity != ? OR measurement_unit != ?) AND dish_id = ? AND ingredient_id = ?"
        db.execSQL(
            query,
            arrayOf(
                if(quantity.isEmpty()) null else quantity.toDouble(),
                measurementUnit,
                if(quantity.isEmpty()) null else quantity.toDouble(),
                measurementUnit,
                dishId.toString(),
                ingredientId.toString()
            )
        )
    }

    // INSERT INTO dish_ingredients (quantity, measurement_unit, ingredient_id, dish_id) VALUES(1.5, "Cups", 1, 1);
    private suspend fun insertOneIngredientForDish(
        quantity: String, //
        measurementUnit: String,
        ingredientId: Int,
        dishId: Int
    ) = withContext(Dispatchers.IO) {

        Log.d("addToDB insertOne", quantity)

        val query =
            "INSERT INTO dish_ingredients (quantity, measurement_unit, ingredient_id, dish_id) VALUES(?, ?, ?, ?)"
        db.execSQL(
            query,
            arrayOf(
                if(quantity.isEmpty()) null else quantity.toDouble(),
                measurementUnit,
                ingredientId.toString(),
                dishId.toString()
            )
        )
    }

    override suspend fun deleteUserDish(userId: Int, dishId: Int) = withContext(Dispatchers.IO) {
        val query = "DELETE FROM dishes WHERE user_id = ? AND dish_id = ?"
        db.execSQL(query, arrayOf(userId.toString(), dishId.toString()))
    }

    // INSERT OR IGNORE INTO bookmarks (user_id, dish_id) VALUES(1, 1);
    override suspend fun addUserBookMark(userId: Int, dishId: Int) = withContext(Dispatchers.IO) {
        val query = "INSERT INTO bookmarks (user_id, dish_id) VALUES(?, ?)"
        db.execSQL(query, arrayOf(userId.toString(), dishId.toString()))
    }

    //  DELETE FROM bookmarks
//  WHERE user_id = 1 AND dish_id = 1;
    override suspend fun deleteUserBookMark(userId: Int, dishId: Int) =
        withContext(Dispatchers.IO) {
            val query = "DELETE FROM bookmarks WHERE user_id = ? AND dish_id = ?"
            db.execSQL(query, arrayOf(userId.toString(), dishId.toString()))
        }

    override suspend fun getUserAddedDishes(userId: Int): List<Dish> = withContext(Dispatchers.IO) {

        //  SELECT dish_id, dish_name, servings, prep_time_hours, prep_time_minutes FROM dishes WHERE user_id = 1;

        val query =
            "SELECT dish_id, dish_name, servings, prep_time_hours, prep_time_minutes FROM dishes WHERE user_id = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        val dishes = mutableListOf<Dish>()

        // Iterate through the cursor
        if (cursor.moveToFirst()) {
            do {
                val dish = Dish(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("dish_id")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("dish_name")),
                    servings = cursor.getInt(cursor.getColumnIndexOrThrow("servings")).toString(),
                    prepTime = PrepTime(
                        hour = cursor.getInt(cursor.getColumnIndexOrThrow("prep_time_hours"))
                            .toString(),
                        minute = cursor.getInt(cursor.getColumnIndexOrThrow("prep_time_minutes"))
                            .toString()
                    )
                )
                dishes.add(dish)

            } while (cursor.moveToNext())
        } else {
            cursor.close()
            return@withContext emptyList()
        }

        cursor.close()
        return@withContext dishes
    }

//    SELECT dishes.dish_id, dishes.dish_name, dishes.servings, dishes.prep_time_hours, dishes.prep_time_minutes
//    FROM dishes
//    JOIN bookmarks ON dishes.dish_id = bookmarks.dish_id
//    JOIN users ON bookmarks.user_id = users.user_id
//    WHERE users.user_id = 1;

    // find out the many dishes that are bookmarked by one user
    // link dishes to bookmark
    // link bookmark to user
    override suspend fun getUserBookMarks(userId: Int): List<Dish> = withContext(Dispatchers.IO) {
        val query =
            "SELECT dishes.dish_id, dishes.dish_name, dishes.servings, dishes.prep_time_hours, dishes.prep_time_minutes FROM dishes " +
                    "JOIN bookmarks ON dishes.dish_id = bookmarks.dish_id " +
                    "JOIN users ON bookmarks.user_id = users.user_id WHERE users.user_id = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        val dishes = mutableListOf<Dish>()

        // Iterate through the cursor
        if (cursor.moveToFirst()) {
            do {
                val dish = Dish(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("dish_id")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("dish_name")),
                    servings = cursor.getInt(cursor.getColumnIndexOrThrow("servings")).toString(),
                    prepTime = PrepTime(
                        hour = cursor.getInt(cursor.getColumnIndexOrThrow("prep_time_hours"))
                            .toString(),
                        minute = cursor.getInt(cursor.getColumnIndexOrThrow("prep_time_minutes"))
                            .toString()
                    )
                )
                dishes.add(dish)
            } while (cursor.moveToNext())
        } else {
            cursor.close()
            return@withContext emptyList()
        }

        cursor.close()
        return@withContext dishes
    }


    override suspend fun getAllDishesByNameSearch(search: String): List<Dish> =
        withContext(Dispatchers.IO) {
            // SELECT dish_id, dish_name, servings, prep_time_hours, prep_time_minutes from dishes WHERE dish_name = 'Soup milk';

            val query =
                "SELECT dish_id, dish_name, servings, prep_time_hours, prep_time_minutes from dishes WHERE dish_name like ?"
            val cursor = db.rawQuery(query, arrayOf("%$search%"))
            val dishes = mutableListOf<Dish>()

            // Iterate through the cursor
            if (cursor.moveToFirst()) {
                do {
                    val dish = Dish(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("dish_id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("dish_name")),
                        servings = cursor.getInt(cursor.getColumnIndexOrThrow("servings"))
                            .toString(),
                        prepTime = PrepTime(
                            hour = cursor.getInt(cursor.getColumnIndexOrThrow("prep_time_hours"))
                                .toString(),
                            minute = cursor.getInt(cursor.getColumnIndexOrThrow("prep_time_minutes"))
                                .toString()
                        )
                    )
                    dishes.add(dish)

                } while (cursor.moveToNext())
            } else {
                cursor.close()
                return@withContext emptyList()
            }
            cursor.close()
            return@withContext dishes
        }




    override suspend fun getAllDishes(): List<Dish> = withContext(Dispatchers.IO) {
        // SELECT dish_id, dish_name, servings, prep_time_hours, prep_time_minutes from dishes;

        val query =
            "SELECT dish_id, dish_name, servings, prep_time_hours, prep_time_minutes from dishes"
        val cursor = db.rawQuery(query, null)
        val dishes = mutableListOf<Dish>()

        // Iterate through the cursor
        if (cursor.moveToFirst()) {
            do {
                val dish = Dish(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("dish_id")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("dish_name")),
                    servings = cursor.getInt(cursor.getColumnIndexOrThrow("servings")).toString(),
                    prepTime = PrepTime(
                        hour = cursor.getInt(cursor.getColumnIndexOrThrow("prep_time_hours"))
                            .toString(),
                        minute = cursor.getInt(cursor.getColumnIndexOrThrow("prep_time_minutes"))
                            .toString()
                    )
                )
                dishes.add(dish)

            } while (cursor.moveToNext())
        } else {
            cursor.close()
            return@withContext emptyList()
        }
        cursor.close()
        return@withContext dishes
    }

    override suspend fun getDish(dishId: Int): Dish? = withContext(Dispatchers.IO) {

        // select dish_id, dish_name, servings, prep_time_hours, prep_time_minutes from dishes WHERE dish_id = 1;

        val query =
            "SELECT dish_id, dish_name, servings, prep_time_hours, prep_time_minutes FROM dishes WHERE dish_id = ?"
        val cursor = db.rawQuery(query, arrayOf(dishId.toString()))

        var dish: Dish? = null

        // Iterate through the cursor
        if (cursor.moveToFirst()) {
            dish = Dish(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("dish_id")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("dish_name")),
                servings = cursor.getInt(cursor.getColumnIndexOrThrow("servings")).toString(),
                prepTime = PrepTime(
                    hour = cursor.getInt(cursor.getColumnIndexOrThrow("prep_time_hours"))
                        .toString(),
                    minute = cursor.getInt(cursor.getColumnIndexOrThrow("prep_time_minutes"))
                        .toString()
                )
            )
        }
        cursor.close()

        return@withContext dish
    }



    override suspend fun didUserCreateDish(userId: Int, dishId: Int): Boolean =
        withContext(Dispatchers.IO) {
            // Select * from dishes where user_id = 1 AND dish_id = 1;

            val query = "SELECT * from dishes WHERE user_id = ? AND dish_id = ?"
            val cursor = db.rawQuery(query, arrayOf(userId.toString(), dishId.toString()))

            // Iterate through the cursor
            if (cursor.moveToFirst()) {
                cursor.close()
                return@withContext true
            }

            cursor.close()
            return@withContext false
        }

    override suspend fun didUserSaveDish(userId: Int, dishId: Int): Boolean =
        withContext(Dispatchers.IO) {
            // Select * from bookmarks where user_id = 1 AND dish_id = 1;

            val query = "SELECT * from bookmarks WHERE user_id = ? AND dish_id = ?"
            val cursor = db.rawQuery(query, arrayOf(userId.toString(), dishId.toString()))

            // Iterate through the cursor
            if (cursor.moveToFirst()) {
                cursor.close()
                return@withContext true
            }

            cursor.close()
            return@withContext false
        }
}