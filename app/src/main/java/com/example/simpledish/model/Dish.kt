package com.example.simpledish.model

import kotlin.math.min

data class Dish(
    val id: Int,
    val name: String,
    val servings: String,
    val prepTime: PrepTime
)

data class PrepTime(val hour: String, val minute: String){
    override fun toString(): String {
        var hourString = ""
        var minString = ""

        when{
            hour == "" -> hourString = ""
            hour.toInt() > 1 -> hourString = "$hour hrs"
            hour.toInt() == 1 -> hourString =  "$hour hr"
            hour.toInt() == 0 -> hourString =  ""
        }
        when{
            minute == "" -> hourString = ""
            minute.toInt() > 1 -> minString = "$minute mins"
            minute.toInt() == 1 -> minString =  "$minute min"
            minute.toInt() == 0 -> minString =  ""
        }
        return "$hourString $minString"
    }
}

// for dish data:
// select dish_name, servings, prep_time_hours, prep_time_minutes from dishes where dish_id = ?;

//for ingredient list:
// select ingredient_name, measurement_unit, quantity from ingredients natural join dish_ingredients where dish_id = ?;

// both
// select dish_name, servings, prep_time_hours, prep_time_minutes, ingredient_name, measurement_unit, quantity from dishes natural join (select * from ingredients natural join dish_ingredients);