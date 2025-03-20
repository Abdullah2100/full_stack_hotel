package com.example.hotel_mobile.Di

import android.content.Context
import android.util.Log
import androidx.multidex.BuildConfig
import androidx.room.Room
import com.example.hotel_mobile.Data.Room.AuthDao
import com.example.hotel_mobile.Data.Room.AuthDataBase
import com.example.hotel_mobile.Util.General
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Singleton
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import javax.inject.Qualifier


@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher



@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher


@Module
@InstallIn(SingletonComponent::class)
class GeneralModule {
    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {

            engine {
                connectTimeout = 60_000
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

<<<<<<< Updated upstream
//            install(DefaultRequest) {
//                header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded)
//            }
=======
            install(Auth) {
                bearer {

                    loadTokens {
                       val authData = authDao.getAuthData()

                       authData.let { it->
                           BearerTokens(it!!.token, it.refreshToken)
                       }

                    }

                    refreshTokens {
                        try {

                            val response = client.post("${General.BASED_URL}/refreshToken/refresh") {
                                setBody(mapOf("refreshToken" to General.authData?.refreshToken))
                                contentType(ContentType.Application.Json)
                            }

                            if (response.status.value == 200) {
                                val newTokenData = response.body<AuthResultDto>()

                                authDao.saveAuthData(AuthModleEntity(0,newTokenData.accessToken,newTokenData.refreshToken))

                                return@refreshTokens BearerTokens(
                                    accessToken = newTokenData.accessToken,
                                    refreshToken = newTokenData.refreshToken
                                )
                            }
                        } catch (e: Exception) {
                            Log.e("Auth", "Failed to refresh token: ${e.message}")
                        }
                        null
                    }



                }
            }
>>>>>>> Stashed changes

        }
    }


    @Provides
    @Singleton
    fun generalContext(@ApplicationContext context: Context): Context {
        return context
    }


    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main


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

}