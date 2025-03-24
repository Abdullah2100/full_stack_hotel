package com.example.hotel_mobile.Dto

import com.example.hotel_mobile.services.kSerializeChanger.LocalDateTimeKserialize
import com.example.hotel_mobile.services.kSerializeChanger.UUIDKserialize
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class PersonDto(
    @Serializable(with = UUIDKserialize::class)
    val personID: UUID? = null,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    @Serializable(with = LocalDateTimeKserialize::class)
    val createdAt: LocalDateTime? = null
)
