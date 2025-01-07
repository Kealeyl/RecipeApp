package com.example.simpledish.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.io.File
import java.io.FileOutputStream


class SimpleDishDatabase(private val context: Context) {

    private val dbName: String = "simpleDish.db"

    // Path to the database in internal storage
    private val databasePath: String
        get() = context.getDatabasePath(dbName).path // calculated dynamically

    // Copy the database from assets to internal storage if it doesn't exist
    private fun copyDatabaseIfNeeded() {
        val dbFile = File(databasePath)
        if (!dbFile.exists()) {
            dbFile.parentFile?.mkdirs()
            context.assets.open(dbName).use { inputStream ->
                FileOutputStream(dbFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
    }

    // open and return the database
    private fun getDatabase(): SQLiteDatabase {
        copyDatabaseIfNeeded()
        return SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE)
    }

    // static
    companion object {
        @Volatile
        private var INSTANCE: SQLiteDatabase? = null

        // SQLiteDatabase API
        fun getInstance(context: Context): SQLiteDatabase {
            return INSTANCE ?: synchronized(this) { // just in case - for race conditions
                SimpleDishDatabase(context).getDatabase().also { INSTANCE = it }
            }
        }

        fun closeDatabase() {
            INSTANCE?.apply {
                if (isOpen) close()
            }
            INSTANCE = null
        }
    }
}




