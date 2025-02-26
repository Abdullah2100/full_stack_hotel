package com.example.hotel_mobile.services.room.authintication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hotel_mobile.Modle.AuthModle

@Dao
interface AuthinticationDAO {

    @Query("select * from authModle limit 1 ")
    suspend fun getToken():AuthModle

    @Query("update authModle set   token = :token,refreshToken= :refreshToken ")
    suspend fun updateNewToken(token:String,refreshToken:String):Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(authinticationData:AuthModle)

}