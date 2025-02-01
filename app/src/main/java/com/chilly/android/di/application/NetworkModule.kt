package com.chilly.android.di.application

import com.chilly.android.data.remote.api.impl.LoginApiImpl
import com.chilly.android.data.remote.api.LoginApi
import com.chilly.android.data.remote.dto.response.ErrorResponse
import com.chilly.android.data.remote.HandledException
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
import javax.inject.Singleton

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
                        val response = cause.response.body<ErrorResponse>()
                        throw HandledException(response)
                    }
                }
            }
        }

        install(ContentNegotiation) {
            json()
        }

        defaultRequest {
            url("http://10.0.2.2:8085/")
            headers.appendIfNameAbsent(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        }
    }

    @Provides
    @Singleton
    fun provideLoginApi(client: HttpClient): LoginApi = LoginApiImpl(client)
}