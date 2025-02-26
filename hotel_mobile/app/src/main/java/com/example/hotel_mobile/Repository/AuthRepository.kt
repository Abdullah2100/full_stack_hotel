package com.example.hotel_mobile.Repository

import com.example.hotel_mobile.Dto.LoginDto
import com.example.hotel_mobile.Dto.SingUpDto
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject
import javax.inject.Singleton


class AuthRepository @Inject constructor(val httpClient: HttpClient) {
    suspend fun loginUser(loginData: LoginDto): HttpResponse {
        return httpClient.post(){
             setBody(loginData)
        }
    }


    suspend fun createNewUser(userData:SingUpDto): HttpResponse {
        return httpClient.post(){
            setBody(userData)
        }
    }


}