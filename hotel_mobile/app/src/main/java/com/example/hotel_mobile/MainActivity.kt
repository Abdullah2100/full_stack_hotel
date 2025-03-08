package com.example.hotel_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.navigation.compose.rememberNavController
 import dagger.hilt.android.AndroidEntryPoint
import com.example.hotel_mobile.View.navigation.NavController

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

                val navyController = rememberNavController()
                NavController(navyController)

        }
    }
}
