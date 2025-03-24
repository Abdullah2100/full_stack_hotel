package com.example.hotel_mobile.ViewModle

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.hotel_mobile.Data.Repository.AuthRepository
import com.example.hotel_mobile.Data.Repository.HotelRepository
import com.example.hotel_mobile.Data.Room.AuthDao
import com.example.hotel_mobile.Data.Room.AuthModleEntity
import com.example.hotel_mobile.Di.IoDispatcher
import com.example.hotel_mobile.Di.MainDispatcher
import com.example.hotel_mobile.Dto.AuthResultDto
import com.example.hotel_mobile.Dto.LoginDto
import com.example.hotel_mobile.Dto.RoomDto
import com.example.hotel_mobile.Modle.NetworkCallHandler
import com.example.hotel_mobile.Modle.RoomModel
import com.example.hotel_mobile.Modle.Screens
import com.example.hotel_mobile.Modle.enNetworkStatus
import com.example.hotel_mobile.Util.DtoToModule.toRoomModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json.Default.decodeFromString
import javax.inject.Inject

@HiltViewModel
class HomeViewModle @Inject constructor(
    val homeRepository: HotelRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,

    ) : ViewModel() {

    private val _rooms = MutableStateFlow<MutableList<RoomModel>?>(null)
    val rooms = _rooms.asStateFlow()


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    val errorHandling = CoroutineExceptionHandler { _, ex ->
        Log.d("AuthError", ex.message ?: "")
        viewModelScope.launch {
            _errorMessage.update {
                ex.message
            }
        }
    }


    init {
        getRooms(1)
    }

    fun getRooms(pageNumber: Int = 1) {

        viewModelScope.launch(ioDispatcher + errorHandling) {

            when (val result = homeRepository.getRooms(pageNumber)) {
                is NetworkCallHandler.Successful<*> -> {
                    //val roomData = decodeFromString<List<RoomDto>>(result.data.toString())
                    val roomData = result.data as List<RoomDto>
                    val roomDataToMutale = roomData.map { it->it.toRoomModel() }.toMutableList()
                    _rooms.emit(roomDataToMutale)
                }

                is NetworkCallHandler.Error -> {
                    throw Exception(result.data);
                }

                else -> {
                    throw Exception("unexpected Stat");

                }
            }

        }
    }


}