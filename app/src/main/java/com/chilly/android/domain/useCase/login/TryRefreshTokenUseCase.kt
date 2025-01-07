package com.chilly.android.domain.useCase.login

import com.chilly.android.domain.repository.PreferencesRepository
import javax.inject.Inject

class TryRefreshTokenUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {

    // whether operation was successful
    suspend operator fun invoke(): Boolean {
        return true
    }

}