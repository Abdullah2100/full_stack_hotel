package com.example.hotel_mobile.ViewModle

import android.content.Context
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotel_mobile.Data.Repository.HotelRepository
import com.example.hotel_mobile.Di.IoDispatcher
import com.example.hotel_mobile.Di.MainDispatcher
import com.example.hotel_mobile.Dto.LoginDto
import com.example.hotel_mobile.Dto.RoomDto
import com.example.hotel_mobile.Dto.response.BookingResponseDto
import com.example.hotel_mobile.Modle.BookingModel
import com.example.hotel_mobile.Modle.BookingModleHolder
import com.example.hotel_mobile.Modle.NetworkCallHandler
import com.example.hotel_mobile.Modle.RoomModel
import com.example.hotel_mobile.Modle.enDropDownDateType
import com.example.hotel_mobile.Modle.enNetworkStatus
import com.example.hotel_mobile.Util.DtoToModule.toBookingModel
import com.example.hotel_mobile.Util.DtoToModule.toRoomModel
import com.example.hotel_mobile.Util.General
import com.example.hotel_mobile.Util.MoudelToDto.toBookingDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModle @Inject constructor(
    val homeRepository: HotelRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,

    ) : ViewModel() {

    private val _rooms = MutableStateFlow<MutableList<RoomModel>?>(null)
    val rooms = _rooms.asStateFlow()

    private val _statusChange = MutableStateFlow<enNetworkStatus>(enNetworkStatus.None)
    val statusChange: StateFlow<enNetworkStatus> = _statusChange.asStateFlow()

    private val _bookedStartBookingDayAtMonthAndYear = MutableStateFlow<Map<Int, Int>?>(null)
    val bookedStartBookingDayAtMonthAndYear = _bookedStartBookingDayAtMonthAndYear.asStateFlow();


    private val _bookedEndBookingDayAtMonthAndYear = MutableStateFlow<Map<Int, Int>?>(null)
    val bookedEndBookingDayAtMonthAndYear = _bookedEndBookingDayAtMonthAndYear.asStateFlow();


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _bookingData = MutableStateFlow<BookingModleHolder>(
        BookingModleHolder(
            startYear = General.getCurrentYear(),
            startMonth = General.getCurrentMonth(),
            startDay = null,
            startTime = "",
            endYear = General.getCurrentYear(),
            endMonth = General.getCurrentMonth(),
            endDay = null,
            endTime = "",
            roomId = null
        )
    )
    val bookingData: StateFlow<BookingModleHolder> = _bookingData.asStateFlow()


    private val _bookingsList = MutableStateFlow<List<BookingModel>?>(null)
    val bookingsList = _bookingsList.asStateFlow()


    fun handlTheSelectionDialog(
        day: Int,
        month: Int,
        year: Int,
        hour: Int? = 0,
        minit: Int? = 0,
        enDropTyp: enDropDownDateType
    ) {
        viewModelScope.launch {
            when (enDropTyp) {
                enDropDownDateType.YearStartBooking -> {
                    Log.d("timeResultData", "1")
                    if (bookingData.value.startYear != year) {
                        _bookedStartBookingDayAtMonthAndYear.emit(null)
                        getBookedBookingDayAt(
                            year = year,
                            month = bookingData.value.startMonth
                        )
                    }
                    val newData = _bookingData.value.copy(startYear = year)
                    _bookingData.value = newData
                }

                enDropDownDateType.MonthStartBooking -> {
                    Log.d("timeResultData", "2")
                    if (bookingData.value.startMonth != month) {
                        _bookedStartBookingDayAtMonthAndYear.emit(null)
                        getBookedBookingDayAt(
                            month = month, year = bookingData.value.startYear
                        )
                    }
                    val newData = _bookingData.value.copy(startMonth = month)
                    _bookingData.value = newData
                }

                enDropDownDateType.DayStartBooking -> {
                    Log.d("timeResultData", "3")
                    val newData = _bookingData.value.copy(startDay = day)
                    _bookingData.value = newData
                }

                enDropDownDateType.TimeStartBooking -> {
                    Log.d("timeResultData", "4")
                    val newData = _bookingData.value.copy(startTime = "$hour:$minit")
                    _bookingData.value = newData

                }

                enDropDownDateType.YearEndBooking -> {
                    Log.d("timeResultData", "5")
                    if (bookingData.value.endYear != year) {
                        _bookedEndBookingDayAtMonthAndYear.emit(null)

                        getBookedBookingDayAt(
                            year = year,
                            month = bookingData.value.endMonth,
                            dropDownType = enDropDownDateType.MonthEndBooking
                        )
                    }
                    val newData = _bookingData.value.copy(endYear = year)
                    _bookingData.value = newData


                }

                enDropDownDateType.MonthEndBooking -> {
                    Log.d("timeResultData", "6")
                    if (bookingData.value.endMonth != month) {
                        _bookedEndBookingDayAtMonthAndYear.emit(null)
                        getBookedBookingDayAt(
                            year = bookingData.value.endYear,
                            month = month,
                            dropDownType = enDropDownDateType.MonthEndBooking

                        )
                    }

                    val newData = _bookingData.value.copy(endMonth = month)
                    _bookingData.value = newData
                }

                enDropDownDateType.DayEndBooking -> {
                    Log.d("timeResultData", "7")
                    val newData = _bookingData.value.copy(endDay = day)
                    _bookingData.value = newData

                }

                enDropDownDateType.TimeEndBooking -> {
                    Log.d("timeResultData", "8")
                    val newData = _bookingData.value.copy(endTime = "$hour:$minit")
                    _bookingData.value = newData

                }
            }

        }

    }

   suspend fun  settingRoomId(roomId:UUID){


    }

    val errorHandling = CoroutineExceptionHandler { _, ex ->
        viewModelScope.launch {
            _errorMessage.update {
                ex.message
            }
        }
    }


    private fun validateBookingCreation(
        bookingData: BookingModleHolder,
    ): String? {

        var message: String? = null;
        if (bookingData.startDay == null) {
            message = "بداية يوم الحجز لا يمكن ان يكون فارغاا"
        } else if (bookingData.startTime.isNullOrEmpty())
            message = "بداية وقت الحجز لا يمكن ان يكون فارغا"
        else if (bookingData.endDay == null) {
            message = "نهاية يوم الحجز لا يمكن ان يكون فارغاا"
        } else if (bookingData.endTime.isNullOrEmpty())
            message = "نهاية وقت الحجز لا يمكن ان يكون فارغا"


        return message;
    }

    fun getRooms(pageNumber: Int = 1) {
        viewModelScope.launch(ioDispatcher + errorHandling) {

            when (val result = homeRepository.getRooms(pageNumber)) {
                is NetworkCallHandler.Successful<*> -> {
                    val roomData = result.data as List<RoomDto>?
                    if (!roomData.isNullOrEmpty()) {
                        var roomDataToMutale = roomData
                            .map { listData -> listData.toRoomModel() }
                            .toMutableList()

                        _rooms.emit(roomDataToMutale)
                    } else {

                        if (_rooms.value == null)
                            _rooms.emit(mutableListOf<RoomModel>())
                    }
                }

                is NetworkCallHandler.Error -> {
                    if (_rooms.value == null)
                        _rooms.emit(mutableListOf<RoomModel>())


                    throw Exception(result.data?.replace("\"", ""));
                }

                else -> {
                    if (_rooms.value == null)
                        _rooms.emit(mutableListOf<RoomModel>())

                    throw Exception("unexpected Stat");
                }
            }

        }
    }

    fun createBooking(
        bookingData: BookingModleHolder,
        errorMessage: MutableState<String?>,
        showBottomSheet: MutableState<Boolean>,
        roomId: UUID


    ) {
        viewModelScope.launch(mainDispatcher + errorHandling) {
            val newData = _bookingData.value.copy(roomId = roomId)
            _bookingData.emit(newData)

            errorMessage.value = validateBookingCreation(bookingData = bookingData)
            if (errorMessage.value != null) {
                showBottomSheet.value = true
            } else {
                _statusChange.emit(enNetworkStatus.Loading)
                val result = homeRepository.createBooking(
                    bookingData.toBookingDto()
                )

                when (result) {
                    is NetworkCallHandler.Successful<*> -> {

                        _statusChange.emit(enNetworkStatus.Complate)
                    }

                    is NetworkCallHandler.Error -> {
                        _statusChange.emit(enNetworkStatus.Error)
                        throw Exception(result.data?.replace("\"", ""));
                    }

                    else -> {
                        _statusChange.emit(enNetworkStatus.Error)

                        throw Exception("unexpected Stat");
                    }
                }

            }

        }
    }

    fun getBookingData(pageNumber: Int = 1) {
        viewModelScope.launch(mainDispatcher + errorHandling) {

            when (val result = homeRepository.getUserBookings(pageNumber)) {
                is NetworkCallHandler.Successful<*> -> {
                    val bookingDataHolder = result.data as List<BookingResponseDto>?
                    if (!bookingDataHolder.isNullOrEmpty()) {
                        val roomDataToMutale = bookingDataHolder
                            .map { listData -> listData.toBookingModel() }
                            .toMutableList()

                        _bookingsList.emit(roomDataToMutale)
                    } else {

                        if (_bookingsList.value == null)
                            _bookingsList.emit(emptyList())
                    }
                }

                is NetworkCallHandler.Error -> {
                    if (_bookingsList.value == null)
                        _bookingsList.emit(emptyList())


                    throw Exception(result.data?.replace("\"", ""));
                }

                else -> {
                    if (_rooms.value == null)
                        _rooms.emit(mutableListOf<RoomModel>())

                    throw Exception("unexpected Stat");
                }
            }

        }
    }

    fun getBookedBookingDayAt(
        month: Int = General.getCurrentMonth(),
        year: Int = General.getCurrentYear(),
        dropDownType: enDropDownDateType? = enDropDownDateType.MonthStartBooking
    ) {
        viewModelScope.launch(ioDispatcher + errorHandling) {
            val result =
                homeRepository.getBookingDayAtSpecficMonthAndYear(year = year, month = month)
            when (result) {
                is NetworkCallHandler.Successful<*> -> {
                    val bookedDate = result.data as List<String>
                    val bookedDateToMap =
                        bookedDate.map { it.trim().toInt() to it.trim().toInt() }.toMap()
                    if (dropDownType == enDropDownDateType.MonthStartBooking) {
                        _bookedStartBookingDayAtMonthAndYear.emit(
                            if (bookedDateToMap.size > 0) bookedDateToMap else emptyMap()
                        )
                        if (_bookedEndBookingDayAtMonthAndYear.value == null) {
                            _bookedEndBookingDayAtMonthAndYear.emit(
                                if (bookedDateToMap.size > 0) bookedDateToMap else emptyMap()
                            )
                        }
                    } else if (dropDownType == enDropDownDateType.MonthEndBooking) {
                        _bookedEndBookingDayAtMonthAndYear.emit(
                            if (bookedDateToMap.size > 0) bookedDateToMap else emptyMap()
                        )
                    }
                }

                is NetworkCallHandler.Error -> {
                    _statusChange.emit(enNetworkStatus.Error)
                    throw Exception(result.data?.replace("\"", ""));
                }

                else -> {
                    _statusChange.emit(enNetworkStatus.Error)

                    throw Exception("unexpected Stat");
                }
            }
        }

    }

    suspend fun clearErrorMessage() {
        _errorMessage.emit("")
    }

}