package com.example.hotel_mobile.Modle.response

import java.time.LocalDateTime
import java.util.UUID

data class PersonModel(
    val personID: UUID? = null,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val createdAt: LocalDateTime? = null
)
