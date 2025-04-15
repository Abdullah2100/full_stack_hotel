package  com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker.data.model

import android.icu.util.Calendar

data class DatePickerDate(
    var year: Int,
    var month: Int,
    var day: Int
)

object DefaultDate {
    private val calendar = Calendar.getInstance()
    var defaultDate = DatePickerDate(
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        0
    )
}
