package com.example.hotel_mobile.Data.Repository

import com.example.hotel_mobile.Dto.AuthResultDto
import com.example.hotel_mobile.Dto.LoginDto
import com.example.hotel_mobile.Dto.SingUpDto
import com.example.hotel_mobile.Modle.NetworkCallHandler
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException
import java.net.UnknownHostException
import javax.inject.Inject


class AuthRepository @Inject constructor(val httpClient: HttpClient) {

    suspend fun loginUser(loginData: LoginDto): NetworkCallHandler {
        return try {
            val result = httpClient.post("/signIn") {
                setBody(loginData)
                contentType(ContentType.Any)

            }
            NetworkCallHandler.Successful(result.body<AuthResultDto>())
        } catch (e: UnknownHostException) {

            return NetworkCallHandler.Error(e.message)

        } catch (e: IOException) {

            return NetworkCallHandler.Error(e.message)

        } catch (e: Exception) {

            return NetworkCallHandler.Error(e.message)
        }

    }

    suspend fun createNewUser(userData: SingUpDto): NetworkCallHandler {
        return try {
            val result = httpClient.post("/signUp") {
                setBody(userData)
                contentType()
            }
            NetworkCallHandler.Successful(result.body<AuthResultDto>())

        } catch (e: UnknownHostException) {

            return NetworkCallHandler.Error(e.message)

        } catch (e: IOException) {

            return NetworkCallHandler.Error(e.message)

        } catch (e: Exception) {

            return NetworkCallHandler.Error(e.message)
        }
    }

}