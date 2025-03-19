package com.chilly.android.data.remote.api.impl

import com.chilly.android.data.remote.TokenHolder
import com.chilly.android.data.remote.api.UserApi
import com.chilly.android.data.remote.api.getResult
import com.chilly.android.data.remote.api.setAuthorization
import com.chilly.android.data.remote.api.wrappedPut
import com.chilly.android.data.remote.dto.UserDto
import com.chilly.android.data.remote.dto.request.ChangeInfoRequest
import com.chilly.android.data.remote.dto.request.ChangePasswordRequest
import com.chilly.android.data.remote.dto.request.ChangeUsernameRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class UserApiImpl(
    private val httpClient: HttpClient,
    private val tokenHolder: TokenHolder
) : UserApi {

    override suspend fun getLoggedUser(): Result<UserDto> {
       return httpClient.getResult("api/user/me") {
           setAuthorization(tokenHolder)
       }
    }

    override suspend fun editUserInfo(info: ChangeInfoRequest): Result<Unit> {
        return httpClient.wrappedPut("api/user/me") {
            setAuthorization(tokenHolder)
            setBody(info)
        }
    }

    override suspend fun editPhoneAndEmail(info: ChangeUsernameRequest): Result<Unit> {
        return httpClient.wrappedPut("api/auth/username") {
            setAuthorization(tokenHolder)
            setBody(info)
        }
    }

    override suspend fun changePassword(request: ChangePasswordRequest): Result<Unit> {
        return httpClient.wrappedPut("api/password") {
            setAuthorization(tokenHolder)
            setBody(request)
        }
    }

}