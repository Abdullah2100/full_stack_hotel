package com.example.hotel_mobile.Dto

import com.example.hotel_mobile.services.kSerializeChanger.LocalDateTimeKserialize
import com.example.hotel_mobile.services.kSerializeChanger.UUIDKserialize
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class UserDto(
    @Serializable(with = UUIDKserialize::class)
    val userId: UUID? = null,
    @Serializable(with = UUIDKserialize::class)
    val addBy: UUID? = null,  // Nullable UUID
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