package com.chilly.android.domain.useCase.login

import com.chilly.android.data.remote.TokenHolder
import com.chilly.android.data.remote.api.LoginApi
import com.chilly.android.data.remote.dto.request.LoginRequest
import com.chilly.android.data.remote.dto.response.LoginResponse
import com.chilly.android.domain.repository.PreferencesRepository
import timber.log.Timber
import javax.inject.Inject

class LogInUseCase @Inject constructor(
    private val loginApi: LoginApi,
    private val tokenHolder: TokenHolder,
    private val preferencesRepository: PreferencesRepository
) {

    suspend operator fun invoke(
        request: LoginRequest,
    ): Result<LoginResponse> =
        loginApi.login(request)
            .onFailure { exception ->
                Timber.e(exception)
            }
            .onSuccess { response ->
                preferencesRepository.saveRefreshToken(response.refreshToken)
                tokenHolder.accessToken = response.accessToken
            }
}