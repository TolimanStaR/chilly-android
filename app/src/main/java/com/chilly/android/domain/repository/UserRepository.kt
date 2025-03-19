package com.chilly.android.domain.repository

import com.chilly.android.data.remote.dto.UserDto
import com.chilly.android.data.remote.dto.request.ChangePasswordRequest

interface UserRepository {

    suspend fun getLoggedUser(): UserDto

    suspend fun editUserInfo(newInfo: UserDto): Result<UserDto>

    suspend fun changePassword(request: ChangePasswordRequest): Result<Unit>

}