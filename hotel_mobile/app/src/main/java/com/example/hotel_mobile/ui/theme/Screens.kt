package com.example.hotel_mobile.ui.theme

import kotlinx.serialization.Serializable

sealed class Screens {

    @Serializable
    object authGraph:Screens()

    @Serializable
    object login:Screens()

    @Serializable
    object signUp:Screens()

    @Serializable
    object homeGraph:Screens()

    @Serializable
    object home:Screens()
}