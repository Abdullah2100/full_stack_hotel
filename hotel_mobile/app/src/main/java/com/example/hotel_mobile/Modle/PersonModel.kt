package com.example.hotel_mobile.Modle

import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class PersonModel(
    val personID: String? = null,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val createdAt: LocalDateTime? = null
)
