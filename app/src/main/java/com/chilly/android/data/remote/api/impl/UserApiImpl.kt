package com.chilly.android.data.remote.api.impl

import com.chilly.android.data.remote.TokenHolder
import com.chilly.android.data.remote.api.UserApi
import com.chilly.android.data.remote.api.getResult
import com.chilly.android.data.remote.api.setAuthorization
import com.chilly.android.data.remote.dto.UserDto
import io.ktor.client.HttpClient

class UserApiImpl(
    private val httpClient: HttpClient,
    private val tokenHolder: TokenHolder
) : UserApi {

    override suspend fun getLoggedUser(): Result<UserDto> {
       return httpClient.getResult("api/user/me") {
           setAuthorization(tokenHolder)
       }
    }

}