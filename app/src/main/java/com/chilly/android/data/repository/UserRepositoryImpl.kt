package com.chilly.android.data.repository

import com.chilly.android.data.remote.api.UserApi
import com.chilly.android.data.remote.dto.UserDto
import com.chilly.android.data.remote.dto.request.ChangeInfoRequest
import com.chilly.android.data.remote.dto.request.ChangePasswordRequest
import com.chilly.android.data.remote.dto.request.ChangeUsernameRequest
import com.chilly.android.domain.repository.UserRepository
import timber.log.Timber

class UserRepositoryImpl(
    private val userApi: UserApi
) : UserRepository {

    private var _loggedUser: UserDto? = null

    /**
     * May throw exceptions need to be used in runCatching
     */
    override suspend fun getLoggedUser(): UserDto =
        _loggedUser ?: fetchLoggedUser()

    override suspend fun editUserInfo(newInfo: UserDto): Result<UserDto> {
        userApi.editUserInfo(ChangeInfoRequest(name = newInfo.name, lastname = newInfo.lastname))
            .onFailure {
                Timber.e(it)
                return Result.failure(it)
            }
        userApi.editPhoneAndEmail(ChangeUsernameRequest(email = newInfo.email, phone = newInfo.phone))
            .onFailure {
                Timber.e(it)
                return Result.failure(it)
            }

        return userApi.getLoggedUser()
            .onSuccess {
                _loggedUser = it
            }
    }

    override suspend fun changePassword(request: ChangePasswordRequest): Result<Unit> {
        return userApi.changePassword(request)
    }

    private suspend fun fetchLoggedUser(): UserDto {
        return userApi.getLoggedUser().getOrThrow()
            .also { _loggedUser = it }
    }
}