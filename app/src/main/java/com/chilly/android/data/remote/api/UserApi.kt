package com.chilly.android.data.remote.api
import com.chilly.android.data.remote.dto.UserDto
import com.chilly.android.data.remote.dto.request.ChangeInfoRequest
import com.chilly.android.data.remote.dto.request.ChangePasswordRequest
import com.chilly.android.data.remote.dto.request.ChangeUsernameRequest

interface UserApi {
    suspend fun getLoggedUser(): Result<UserDto>

    suspend fun editUserInfo(info: ChangeInfoRequest): Result<Unit>

    suspend fun editPhoneAndEmail(info: ChangeUsernameRequest): Result<Unit>

    suspend fun changePassword(request: ChangePasswordRequest): Result<Unit>

}