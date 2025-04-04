package com.example.hotel_mobile.Modle

import com.example.hotel_mobile.Dto.RoomDto
import com.example.hotel_mobile.services.kSerializeChanger.UUIDKserialize
import kotlinx.serialization.Serializable
import java.util.UUID

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
    data class Room(
       val roomdata:RoomDto
    )
}