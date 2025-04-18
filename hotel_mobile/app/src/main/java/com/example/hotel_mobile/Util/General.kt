package com.example.hotel_mobile.Util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker.data.Constant
import com.example.hotel_mobile.Data.Room.AuthDao
import com.example.hotel_mobile.Data.Room.AuthModleEntity
import com.example.hotel_mobile.Dto.AuthResultDto
import kotlinx.coroutines.flow.MutableStateFlow
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date


object General {

    fun getCalener(): Calendar {
        return Calendar.getInstance();
    }

    fun getCurrentYear(): Int {
        return getCalener().get(Calendar.YEAR)
    }

    fun getCurrentMonth(): Int {
        return getCalener().get(Calendar.MONTH)
    }

    fun getCurrentStartDayAtMonth(): Int {
        return getCalener().get(Calendar.DAY_OF_MONTH)
    }

    fun getCurrentCurrentTime(): String {
        val currentHour =getCalener().get(Calendar.HOUR_OF_DAY)
        val currentMinute =getCalener().get(Calendar.MINUTE)
        return "${currentHour}:${currentMinute}"
    }

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

    fun LocalDateTime.toCustomString():String{
        val year = this.year;
        val month = this.monthValue;
        val day = this.dayOfMonth;
        val hour = this.hour
        val minit = this.minute;
        //val localDateTimeString = "$day-$month-$year $hour:$minit:00 ${if(hour<=12)"AM" else "PM"}"
        val localDateTimeString = "$day-$month-$year"
        Log.d("customStringDate","$this")
        return localDateTimeString;
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

            1 -> {
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

            1 -> {
                Color.Red
            }

            else -> Color.Yellow
        }
    }

    fun convertMonthToName(monthNumber: Int,year:Int): String {
        return Constant.getMonths(year)[monthNumber].name
    }

    fun convertDayToNumber(day: String): Int {
        return when (day.lowercase()) {
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

    fun handlingBookingStatusBackgroundColor(bookingStatus:String): Color {
      return  when(bookingStatus){
             "Pending" -> {
            Color.Yellow
             }
            "Confirmed" ->{
                return Color.Green
                 }
             else->{
                 return Color.Red
                 }
        }
    }


   fun dashStrock ()= Stroke(
    width = 4f,
    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

     fun Uri.toCustomFil(context: Context): File? {
        var file: File? = null;

        try {
            val resolver = context.contentResolver;
            resolver.query(this, null, null, null, null)
                .use {
                        cursor->
                    if(cursor==null) throw Exception("could not accesss Local Storage")

                    cursor.moveToFirst()
                    val column = arrayOf(MediaStore.Images.Media.DATA)
                    val filePath = cursor.getColumnIndex(column[0])
                    file = File(cursor.getString(filePath))

                }
            return  file;
        } catch (e: Exception) {
            throw e;
        }
    }
}
