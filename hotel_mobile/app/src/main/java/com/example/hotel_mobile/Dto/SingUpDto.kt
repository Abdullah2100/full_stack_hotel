package com.example.hotel_mobile.Dto

import com.example.hotel_mobile.services.kSerializeChanger.LocalDateTimeKserialize
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.io.File
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date

@Serializable

data class SingUpDto(
    val name: String,

    val email: String,

    val phone: String,

    val address: String,

    val userName: String, val password: String,

    @Serializable(with=LocalDateTimeKserialize::class)
    val brithDay: LocalDateTime?=null,

    val isVip: Boolean,

    @Transient
    val imagePath: Date?=null

)