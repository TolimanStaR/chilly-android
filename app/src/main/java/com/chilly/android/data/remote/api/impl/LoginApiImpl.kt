package com.chilly.android.data.remote.api.impl

import com.chilly.android.data.remote.api.LoginApi
import com.chilly.android.data.remote.api.postWithResult
import com.chilly.android.data.remote.dto.request.RefreshRequest
import com.chilly.android.data.remote.dto.request.LoginRequest
import com.chilly.android.data.remote.dto.response.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class LoginApiImpl(private val client: HttpClient) : LoginApi {

    override suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return client.postWithResult("api/auth/login") {
            setBody(request)
        }
    }

    override suspend fun refresh(request: RefreshRequest): Result<LoginResponse> {
        return client.postWithResult("api/auth/refresh") {
            setBody(request)
        }
    }

}