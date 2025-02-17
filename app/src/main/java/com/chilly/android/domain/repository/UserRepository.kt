package com.chilly.android.domain.repository

import com.chilly.android.data.remote.dto.UserDto

interface UserRepository {

    suspend fun getLoggedUser(): UserDto

}