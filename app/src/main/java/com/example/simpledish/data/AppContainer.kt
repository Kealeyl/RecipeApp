package com.example.simpledish.data

import android.content.Context
import com.example.simpledish.data.database.SimpleDishDaoImpl
import com.example.simpledish.data.database.SimpleDishDatabase

// App container for Dependency injection
// dependencies that the app requires, they are used across the whole application
interface AppContainer {
    val simpleDishDatabaseRepository: SimpleDishDaoImpl
}

class AppDataContainer(context: Context) : AppContainer {

    //private val simpleDishDAO = SimpleDishDaoImpl(SimpleDishDatabase.getInstance(context))

    // injected into view model
    override val simpleDishDatabaseRepository: SimpleDishDaoImpl by lazy {
        SimpleDishDaoImpl(SimpleDishDatabase.getInstance(context)) // DAO instance, pass DB
    }
}