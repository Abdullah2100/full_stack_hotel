package com.example.hotel_mobile.Di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.hotel_mobile.Data.Room.AuthDao
import com.example.hotel_mobile.Data.Room.AuthDataBase
import com.example.hotel_mobile.Data.Room.AuthModleEntity
import com.example.hotel_mobile.Util.General
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Singleton
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import javax.inject.Qualifier
import com.example.hotel_mobile.Dto.AuthResultDto
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.Parameters


@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher



@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher


@Module
@InstallIn(SingletonComponent::class)
class GeneralModule {


    @Provides
    @Singleton
    fun generalContext(@ApplicationContext context: Context): Context {
        return context
    }


    @Provides
    @Singleton
    fun createAuthDataBase(context: Context): AuthDataBase {
        return  Room.databaseBuilder(
            context,
            AuthDataBase::class.java, "authDB.db"
        )
            .openHelperFactory(General.encryptionFactory("authDB.db"))
            .fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    @Singleton
    fun authDataBase (authDataBase: AuthDataBase): AuthDao {
        return authDataBase.fileDo()
    }

    @Singleton
    @Provides
    fun provideHttpClient(authDao:AuthDao): HttpClient {
        return HttpClient(Android) {

            engine {
                connectTimeout = 60_000
            }


            install(Auth) {
                bearer {
//                  sendWithoutRequest { true }
                    loadTokens {
                        BearerTokens(
                            General.authData.value?.token?:"",
                            General.authData.value?.refreshToken ?:""
                        )
                    }

                    refreshTokens {
                        val refreshToken = client.
                        post("${General.BASED_URL}/refreshToken/refresh") {
                            url {
                                parameters.append("tokenHolder", General.authData.value?.refreshToken ?: "")
                            }
                        }.body<AuthResultDto>()
                        // Update saved tokens
                        General.updateSavedToken(authDao, refreshToken)
                        BearerTokens(
                            accessToken = refreshToken.accessToken,
                            refreshToken = refreshToken.refreshToken
                        )
                    }
                }
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v("Logger Ktor =>", message)
                    }
                }
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }


        }
    }



    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main




}