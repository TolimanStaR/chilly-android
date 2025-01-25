package com.chilly.android.presentation.sign_up

import com.chilly.android.di.screens.SignUpScope
import jakarta.inject.Inject
import kotlinx.coroutines.flow.FlowCollector

@SignUpScope
class SignUpNewsCollector @Inject constructor() : FlowCollector<SignUpNews> {

    override suspend fun emit(value: SignUpNews) {
        TODO("Not yet implemented")
    }
}