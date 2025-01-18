package com.chilly.android.domain.useCase.login

import com.chilly.android.data.remote.api.LoginApi
import com.chilly.android.data.remote.dto.request.RefreshRequest
import com.chilly.android.domain.repository.PreferencesRepository
import timber.log.Timber
import javax.inject.Inject

class TryRefreshTokenUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val loginApi: LoginApi
) {

    // whether operation was successful
    suspend operator fun invoke(): Boolean {
        val refreshToken = preferencesRepository.getSavedRefreshToken() ?: run {
            Timber.e("no saved refresh token")
            return false
        }
        val response = loginApi.refresh(RefreshRequest(refreshToken))
            .getOrElse {
                Timber.e(it)
                return false
            }

        preferencesRepository.saveRefreshToken(response.refreshToken)
        Timber.i("accessToken = ${response.accessToken}")
        return true
    }

}