package com.example.hotel_mobile.Di

import android.content.Context
import androidx.multidex.BuildConfig
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GeneralModule {
    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android){
            install(DefaultRequest){
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                url("")
            }
            install(ContentNegotiation){
                json(Json)
            }
        }
    }


    @Provides
    @Singleton
    fun generalContext(@ApplicationContext context: Context): Context {
        return context
    }


}