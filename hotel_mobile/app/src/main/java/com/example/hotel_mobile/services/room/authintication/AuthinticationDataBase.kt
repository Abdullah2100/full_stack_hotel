package com.example.hotel_mobile.services.room.authintication

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hotel_mobile.Modle.AuthModle
import javax.inject.Singleton

@Singleton
@Database(entities = [AuthModle::class], version = 1)
abstract class AuthinticationDataBase:RoomDatabase() {
    abstract fun authinticationAdo(): AuthinticationDAO

}