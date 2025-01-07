package com.example.simpledish.model

data class Ingredient(
    val ingredientId: Int,
    val ingredientName: String,
    val measurementUnit: String,
    val quantity: String){

    fun amount() : String{
        return "$quantity $measurementUnit"
    }
}
