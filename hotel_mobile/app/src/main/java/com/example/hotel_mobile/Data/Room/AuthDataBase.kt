package com.example.hotel_mobile.Data.Room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AuthModleEntity::class], version = 1, exportSchema = false)
abstract class AuthDataBase: RoomDatabase() {
    abstract fun fileDo():AuthDao
}