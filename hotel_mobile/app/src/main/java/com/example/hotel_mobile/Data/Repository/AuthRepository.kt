package com.example.hotel_mobile.Data.Repository

import android.util.Log
import com.example.hotel_mobile.Dto.LoginDto
import com.example.hotel_mobile.Dto.SingUpDto
import com.example.hotel_mobile.Modle.NetworkCallHandler
import com.example.hotel_mobile.Util.General
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException
import java.net.UnknownHostException
import javax.inject.Inject


class AuthRepository @Inject constructor(private val httpClient: HttpClient) {

    suspend fun loginUser(loginData: LoginDto): NetworkCallHandler {
        return try {
            val result = httpClient
                    .post("${General.BASED_URL}/user/signIn") {
                        setBody(loginData)
                        contentType(ContentType.Application.Json)
                    }

            if (result.status == HttpStatusCode.OK) {
                NetworkCallHandler.Successful(result.body<String>())
            } else {

                NetworkCallHandler.Error(result.body())
            }

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

            val result = httpClient
                .post("${General.BASED_URL}/user/signUp") {
                        setBody(MultiPartFormDataContent(
                            formData {
                                append("name", userData.name)
                                append("email", userData.email)
                                append("phone", userData.phone)
                                append("address", userData.address)
                                append("userName", userData.userName)
                                append("password", userData.password)
                                append("brithDay", userData.brithDay.toString())
                                append("isVip", false)
                            }
                        ))
                }

            if (result.status == HttpStatusCode.Created) {

                NetworkCallHandler.Successful(result.body<String>())
            } else {

                NetworkCallHandler.Error(result.body())

            }


        } catch (e: UnknownHostException) {

            return NetworkCallHandler.Error(e.message)

        } catch (e: IOException) {

            return NetworkCallHandler.Error(e.message)

        } catch (e: Exception) {

            return NetworkCallHandler.Error(e.message)
        }

    }

}