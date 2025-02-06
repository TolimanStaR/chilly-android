package com.chilly.android.data.remote.api.impl

import com.chilly.android.data.remote.api.PasswordRecoveryApi
import com.chilly.android.data.remote.api.postWithResult
import com.chilly.android.data.remote.api.wrappedPost
import com.chilly.android.data.remote.api.wrappedPut
import com.chilly.android.data.remote.dto.request.RecoverPasswordRequest
import com.chilly.android.data.remote.dto.request.SendCodeRequest
import com.chilly.android.data.remote.dto.request.VerifyCodeRequest
import com.chilly.android.data.remote.dto.response.VerificationResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class PasswordRecoveryApiImpl(
    private val httpClient: HttpClient
) : PasswordRecoveryApi {

    override suspend fun sendCode(request: SendCodeRequest): Result<Unit> {
        return httpClient.wrappedPost("api/email_code") {
            setBody(request)
        }
    }

    override suspend fun verifyCode(request: VerifyCodeRequest): Result<VerificationResponse> {
        return httpClient.postWithResult("api/email_code/verification") {
            setBody(request)
        }
    }

    override suspend fun recoverPassword(request: RecoverPasswordRequest): Result<Unit> {
        return httpClient.wrappedPut("api/password/recovery") {
            setBody(request)
        }
    }
}