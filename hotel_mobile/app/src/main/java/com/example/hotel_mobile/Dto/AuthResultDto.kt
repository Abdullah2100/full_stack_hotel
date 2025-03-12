package com.example.hotel_mobile.Dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthResultDto(
    var accessToken: String="",
    var refreshToken: String = ""
)