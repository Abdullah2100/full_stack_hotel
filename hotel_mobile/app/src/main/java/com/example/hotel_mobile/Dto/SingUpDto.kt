package com.example.hotel_mobile.Dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.io.File
import java.util.Calendar
import java.util.Date

@Serializable

data class SingUpDto(
    val name: String,

    val email: String,

    val phone: String,

    val address: String,

    val userName: String, val password: String,

    @Transient
    val brithDay: Date?=null,

    val isVip: Boolean,

    @Transient
    val imagePath: Date?=null

)