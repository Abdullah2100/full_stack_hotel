package com.example.hotel_mobile.ViewModle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotel_mobile.Data.Repository.HotelRepository
import com.example.hotel_mobile.Di.IoDispatcher
import com.example.hotel_mobile.Dto.RoomDto
import com.example.hotel_mobile.Modle.BookingModel
import com.example.hotel_mobile.Modle.NetworkCallHandler
import com.example.hotel_mobile.Modle.RoomModel
import com.example.hotel_mobile.Modle.enNetworkStatus
import com.example.hotel_mobile.Util.DtoToModule.toRoomModel
import com.example.hotel_mobile.Util.MoudelToDto.toBookingDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModle @Inject constructor(
    val homeRepository: HotelRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,

    ) : ViewModel() {

    private val _rooms = MutableStateFlow<MutableList<RoomModel>?>(null)
    val rooms = _rooms.asStateFlow()

    private val _statusChange = MutableStateFlow<enNetworkStatus>(enNetworkStatus.None)
    val statusChange: StateFlow<enNetworkStatus> = _statusChange.asStateFlow()



    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    val errorHandling = CoroutineExceptionHandler { _, ex ->
        viewModelScope.launch {
            _errorMessage.update {
                ex.message
            }
        }
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


                    throw Exception(result.data?.replace("\"",""));
                }

                else -> {
                    if (_rooms.value == null)
                        _rooms.emit(mutableListOf<RoomModel>())

                    throw Exception("unexpected Stat");
                }
            }

        }
    }

    fun createBooking(bookingData: BookingModel) {
        viewModelScope.launch(ioDispatcher + errorHandling) {
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
                    throw Exception(result.data?.replace("\"",""));
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