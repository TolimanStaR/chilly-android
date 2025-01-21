package com.chilly.android.data.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenHolder @Inject constructor() {

    var accessToken: String? = null

    fun requireToken(): String = accessToken ?: throw IllegalStateException()

}