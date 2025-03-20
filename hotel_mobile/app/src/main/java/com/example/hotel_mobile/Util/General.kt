package com.example.hotel_mobile.Util

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hotel_mobile.Data.Room.AuthDataBase
import com.example.hotel_mobile.Data.Room.AuthModleEntity
<<<<<<< Updated upstream
=======
import com.example.hotel_mobile.Dto.AuthResultDto
import com.example.hotel_mobile.Dto.RoomDto
import com.example.hotel_mobile.Modle.ImageModel
import com.example.hotel_mobile.Modle.PersonModel
import com.example.hotel_mobile.Modle.RoomModel
import com.example.hotel_mobile.Modle.UserModel
>>>>>>> Stashed changes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.LocalDateTime
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import java.util.Date
<<<<<<< Updated upstream

object General {

    var authData = MutableStateFlow<AuthModleEntity?>(null)
=======
import java.util.UUID
import javax.inject.Inject

object General  {

       var authData:AuthModleEntity?=null;
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
        return date
    }
=======
           date.date = day
           date.month = month
           date.year = year

           return date
       }

>>>>>>> Stashed changes
}