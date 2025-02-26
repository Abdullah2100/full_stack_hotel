package com.example.hotel_mobile.Dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginDto (var userNameOrEmail:String,var password:String)