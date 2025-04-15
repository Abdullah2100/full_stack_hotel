package  com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker.ui.viewmodel

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import  com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker.data.Constant
import  com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker.data.model.DatePickerDate
import  com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker.data.model.Month
import  com.example.hotel_mobile.CustomDatePicker.vsnappy1.datepicker.ui.model.DatePickerUiState
import com.example.hotel_mobile.Util.General
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class DatePickerViewModel : ViewModel() {

    private var _uiState: MutableLiveData<DatePickerUiState> = MutableLiveData(DatePickerUiState())
    val uiState: LiveData<DatePickerUiState> = _uiState
    private lateinit var availableMonths: List<Month>

    private var _availableMonths: MutableLiveData<List<Month>> = MutableLiveData(emptyList())



    override fun onCleared() {
        updateSelectedDayAndMonth(0)
        super.onCleared()
    }

    private fun updateVisibleMonth(months: List<Month>) {
        _availableMonths.value?.apply {
            _availableMonths.value = months
        }
    }

    private fun updateCurrentVisibleMonth(month: Int) {

        _uiState.value?.apply {
            _uiState.value = this.copy(
                currentVisibleMonth = availableMonths[month],
                selectedMonthIndex = month
            )
        }


    }

    fun updateSelectedMonthIndex(index: Int) {
        _uiState.value?.apply {
            _uiState.value = this.copy(
                selectedMonthIndex = index,
                currentVisibleMonth = _availableMonths.value!![index % 12]
            )
        }

    }


    fun updateSelectedDayAndMonth(day: Int) {

        _uiState.value = _uiState.value?.let {
            _uiState.value?.copy(
                selectedDayOfMonth = day,
                selectedMonth = it.currentVisibleMonth
            )
        }
    }



    fun updateSelectedYearIndex(index: Int) {
        availableMonths = Constant.getMonths(Constant.years[index])
        _uiState.value = _uiState.value?.copy(
            selectedYearIndex = index,
            selectedYear = Constant.years[index],
            currentVisibleMonth = availableMonths[_uiState.value?.currentVisibleMonth?.number ?: 0]
        )
    }


    fun setDate(
        date: DatePickerDate,
        calendar: Calendar = Calendar.getInstance()
    ) {
        val yearMin = Constant.years.first()
        val yearMax = Constant.years.last()

        if (date.year < yearMin || date.year > yearMax) {
            throw IllegalArgumentException("Invalid year: ${date.year}, The year value must be between $yearMin (inclusive) to $yearMax (inclusive).")
        }
        if (date.month < 0 || date.month > 11) {
            throw IllegalArgumentException("Invalid month: ${date.month}, The month value must be between 0 (inclusive) to 11 (inclusive).")
        }

        calendar[Calendar.YEAR] = date.year
        calendar[Calendar.MONTH] = date.month

        val maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        if (date.day == 0) {
        } else if (date.day < 1) {
            throw IllegalArgumentException("Invalid day: ${date.day}, The day value must be greater than zero.")
        }
        if (date.day > maxDays) {
            throw IllegalArgumentException("Invalid day: ${date.day}, The day value must be less than equal to $maxDays for given month (${Constant.getMonths()[date.month]}).")
        }

        val index = Constant.years.indexOf(date.year)
        updateSelectedYearIndex(index)
        updateCurrentVisibleMonth(date.month)
        updateSelectedDayAndMonth(date.day)
        val months = Constant.getMonths(date.year)
        updateVisibleMonth(months)

    }
}