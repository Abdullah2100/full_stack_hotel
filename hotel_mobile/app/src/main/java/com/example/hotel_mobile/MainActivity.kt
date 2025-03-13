package com.example.hotel_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.hotel_mobile.Data.Room.AuthDataBase
import com.example.hotel_mobile.Data.Room.AuthModleEntity
import com.example.hotel_mobile.Util.General
import dagger.hilt.android.AndroidEntryPoint
import com.example.hotel_mobile.View.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            General.authDataBase.emit(
                Room.databaseBuilder(
                    applicationContext,
                    AuthDataBase::class.java, "authDB.db"
                )
                    .openHelperFactory(General.encryptionFactory("authDB.db"))
                    .fallbackToDestructiveMigration()
                    .build()
            )
        }



        enableEdgeToEdge()
        setContent {

            val navyController = rememberNavController()
            NavController(navyController)

        }
    }
}
