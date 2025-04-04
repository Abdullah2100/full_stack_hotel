package com.example.hotel_mobile.Util

import android.R.string
import androidx.compose.ui.graphics.Color
import com.example.hotel_mobile.Data.Room.AuthDao
import com.example.hotel_mobile.Data.Room.AuthModleEntity
import com.example.hotel_mobile.Dto.AuthResultDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date


object General {


    var authData = MutableStateFlow<AuthModleEntity?>(null);

    const val BASED_URL = "http://10.0.2.2:5266/api"


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
    fun Long.toLocalDate(): LocalDate {
        return Instant.ofEpochMilli(this)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }


    suspend fun updateSavedToken(ado: AuthDao, authData: AuthResultDto) {
        val authDataHolder = AuthModleEntity(0, authData.accessToken, authData.refreshToken)
        if (authData.accessToken.length > 0) {
            ado.saveAuthData(authDataHolder)
            General.authData.emit(authDataHolder)// = authDataHolder;
        }
    }

    fun convertMilisecondToLocalDateTime(miliSecond: Long?): LocalDateTime? {

        return when {
            miliSecond == null -> null;
            else -> {
                return Instant.ofEpochMilli(miliSecond)
                    .atZone(ZoneId.systemDefault())  // Adjust to system's timezone
                    .toLocalDateTime()
            }
        }
    }
     fun convertRoomStateToText(status: Int): String {
        return when (status) {
            0 -> {
                "متاح"
            }

            1-> {
                "محظور"
            }

            else -> "تحت الصيانة"
        }
    }

    fun convertRoomStateToColor(status: Int): Color {
        return when (status) {
            0 -> {
                Color.Green
            }

            1-> {
                Color.Red
            }

            else -> Color.Yellow
        }
    }


    fun convertDayToNumber(day: String): Int {
        return when(day.lowercase()) {
            "monday" -> 1
            "tuesday" -> 2
            "wednesday" -> 3
            "thursday" -> 4
            "friday" -> 5
            "saturday" -> 6
            "sunday" -> 7
            else -> 0
        }
    }
}