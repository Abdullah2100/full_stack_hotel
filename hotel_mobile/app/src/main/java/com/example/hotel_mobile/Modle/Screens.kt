package com.example.hotel_mobile.Modle

import com.example.hotel_mobile.Dto.RoomDto
import kotlinx.serialization.Serializable

object Screens {

    @Serializable
    object authGraph

    @Serializable
    object login

    @Serializable
    object signUp

    @Serializable
    object homeGraph

    @Serializable
    object home

    @Serializable
    object booking

    @Serializable
    object myRooms

    @Serializable
    object createNewRoom

    @Serializable
    object bookingForMyRoom

    @Serializable
    data class Room(
       val roomdata:RoomDto
    )
}