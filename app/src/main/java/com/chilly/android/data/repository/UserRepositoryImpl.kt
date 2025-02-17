package com.chilly.android.data.repository

import com.chilly.android.data.remote.api.UserApi
import com.chilly.android.data.remote.dto.UserDto
import com.chilly.android.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userApi: UserApi
) : UserRepository {

    private var _loggedUser: UserDto? = null

    /**
     * May throw exceptions need to be used in runCatching
     */
    override suspend fun getLoggedUser(): UserDto =
        _loggedUser ?: fetchLoggedUser()

    private suspend fun fetchLoggedUser(): UserDto {
        return userApi.getLoggedUser().getOrThrow()
            .also { _loggedUser = it }
    }
}