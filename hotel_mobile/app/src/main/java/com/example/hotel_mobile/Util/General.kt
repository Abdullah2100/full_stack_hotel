package com.example.hotel_mobile.Util

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hotel_mobile.Data.Room.AuthDao
import com.example.hotel_mobile.Data.Room.AuthDataBase
import com.example.hotel_mobile.Data.Room.AuthModleEntity
import com.example.hotel_mobile.Dto.AuthResultDto
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import java.util.Date

object General {


       var authData:AuthModleEntity?=null;

    val BASED_URL = "http://10.0.2.2:5266/api"


    fun encryptionFactory(databaseName: String): SupportFactory {
        val passPhraseBytes = SQLiteDatabase.getBytes(databaseName.toCharArray())
        return SupportFactory(passPhraseBytes)
    }

    fun String.toDate(): Date {
        var date = Date()
        val stringDateToList = this.split("/")
        val year = stringDateToList[2].toInt()
        val month = stringDateToList[1].toInt()
        val day = stringDateToList[0].toInt()


           date.date = day
           date.month = month
           date.year = year

           return date
       }

    suspend fun updateSavedToken (ado:AuthDao, authData:AuthResultDto){
        val authDataHolder = AuthModleEntity(0,authData.accessToken,authData.refreshToken)
        if(authData.accessToken.length>0){
            ado.saveAuthData(authDataHolder)
            General.authData=authDataHolder;
        }
    }

}