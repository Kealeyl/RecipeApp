package com.example.simpledish

import android.app.Application
import com.example.simpledish.data.AppContainer
import com.example.simpledish.data.AppDataContainer
import com.example.simpledish.data.database.SimpleDishDatabase

// connect the application object to the application container
// add to Android manifest so app uses the application class
class SimpleDishAppApplication : Application() {
    // AppContainer instance used by the app to obtain dependencies
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        SimpleDishDatabase.closeDatabase()
    }

}