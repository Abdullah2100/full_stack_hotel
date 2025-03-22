package com.example.hotel_mobile.Data.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AuthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAuthData(authData :AuthModleEntity)

    @Query("SELECT * FROM AuthModleEntity")
    suspend  fun getAuthData():AuthModleEntity?

}