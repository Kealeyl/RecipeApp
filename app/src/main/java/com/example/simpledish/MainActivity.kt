package com.example.simpledish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.example.simpledish.data.database.SimpleDishDatabase
import com.example.simpledish.ui.theme.SimpleDishTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleDishTheme {
                Surface{
                    SimpleDishApp()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()


    }
}