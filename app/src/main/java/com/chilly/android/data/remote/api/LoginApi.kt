package com.chilly.android.data.remote.api

import com.chilly.android.data.remote.dto.request.SignUpRequest
import com.chilly.android.data.remote.dto.request.LoginRequest
import com.chilly.android.data.remote.dto.request.RefreshRequest
import com.chilly.android.data.remote.dto.response.LoginResponse

interface LoginApi {
    suspend fun login(request: LoginRequest): Result<LoginResponse>
    suspend fun refresh(request: RefreshRequest): Result<LoginResponse>
    suspend fun signUp(request: SignUpRequest): Result<Unit>
}