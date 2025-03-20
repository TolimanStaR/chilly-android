package com.chilly.android.di.application

import com.chilly.android.data.remote.HandledException
import com.chilly.android.data.remote.TokenHolder
import com.chilly.android.data.remote.api.LoginApi
import com.chilly.android.data.remote.api.PasswordRecoveryApi
import com.chilly.android.data.remote.api.QuizApi
import com.chilly.android.data.remote.api.RecommendationApi
import com.chilly.android.data.remote.api.UserApi
import com.chilly.android.data.remote.api.impl.LoginApiImpl
import com.chilly.android.data.remote.api.impl.PasswordRecoveryApiImpl
import com.chilly.android.data.remote.api.impl.QuizApiImpl
import com.chilly.android.data.remote.api.impl.RecommendationApiImpl
import com.chilly.android.data.remote.api.impl.UserApiImpl
import com.chilly.android.data.remote.dto.response.ErrorResponse
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

private const val DEFAULT_URL = "http://188.120.236.240:8085/"

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(OkHttp) {
        expectSuccess = true

        HttpResponseValidator {
            handleResponseExceptionWithRequest { cause, _ ->
                when(cause) {
                    is ClientRequestException -> {
                        val response = runCatching {
                            cause.response.body<ErrorResponse>()
                        }.getOrDefault(
                            ErrorResponse(cause.response.status.value, "Gateway denial")
                        )

                        throw HandledException(response)
                    }
                }
            }
        }

        install(ContentNegotiation) {
            json(
                json = Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                }
            )
        }

        defaultRequest {
            url(DEFAULT_URL)
            headers.appendIfNameAbsent(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        }
    }

    @Provides
    @Singleton
    fun provideLoginApi(client: HttpClient): LoginApi = LoginApiImpl(client)

    @Provides
    @Singleton
    fun provideRecoveryApi(client: HttpClient): PasswordRecoveryApi =
        PasswordRecoveryApiImpl(client)

    @Provides
    @Singleton
    fun provideUserApi(client: HttpClient, tokenHolder: TokenHolder): UserApi =
        UserApiImpl(client, tokenHolder)

    @Provides
    @Singleton
    fun provideQuizApi(client: HttpClient, tokenHolder: TokenHolder): QuizApi =
        QuizApiImpl(client, tokenHolder)

    @Provides
    @Singleton
    fun provideRecommendationApi(client: HttpClient, tokenHolder: TokenHolder): RecommendationApi =
        RecommendationApiImpl(client, tokenHolder)
}