package com.example.hotel_mobile.Dto

import com.example.hotel_mobile.services.kSerializeChanger.LocalDateTimeKserialize
import com.example.hotel_mobile.services.kSerializeChanger.UUIDKserialize
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserDto(
    val userId: String? = null,
    val addBy: String? = null,  // Nullable UUID
    @Serializable(with = LocalDateTimeKserialize::class)
    val brithDay: LocalDateTime? = null,
    val isVip: Boolean? = false,
    val personData: PersonDto,
    val userName: String,
    val password: String,
    val isDeleted: Boolean,
    val imagePath: String? = null,
    val isUser: Boolean? = true
)