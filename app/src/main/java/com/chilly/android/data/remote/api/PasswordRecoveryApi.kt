package com.chilly.android.data.remote.api

import com.chilly.android.data.remote.dto.request.RecoverPasswordRequest
import com.chilly.android.data.remote.dto.request.SendCodeRequest
import com.chilly.android.data.remote.dto.request.VerifyCodeRequest
import com.chilly.android.data.remote.dto.response.VerificationResponse

interface PasswordRecoveryApi {
    suspend fun sendCode(request: SendCodeRequest): Result<Unit>
    suspend fun verifyCode(request: VerifyCodeRequest): Result<VerificationResponse>
    suspend fun recoverPassword(request: RecoverPasswordRequest): Result<Unit>
}