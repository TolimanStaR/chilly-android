package com.chilly.android.di.application

import com.chilly.android.data.remote.api.impl.LoginApiImpl
import com.chilly.android.data.remote.api.LoginApi
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
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