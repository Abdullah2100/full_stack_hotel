package com.example.hotel_mobile.Data.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AuthDao {
    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun saveAuthData(authData :AuthModleEntity)

    @Query("SELECT * FROM AuthModleEntity limit 1")
    suspend  fun getAuthData():AuthModleEntity

}