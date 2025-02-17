package com.chilly.android.data.remote.api
import com.chilly.android.data.remote.dto.UserDto

interface UserApi {
    suspend fun getLoggedUser(): Result<UserDto>
}