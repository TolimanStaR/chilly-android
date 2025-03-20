package com.chilly.android.presentation.common.logic

import androidx.annotation.StringRes
import com.chilly.android.R
import javax.inject.Inject

class FieldValidator @Inject constructor() {

    /**
     * checks the string with the Regular expression that associated with given type
     * if check fails returns resource id for error representation
     */
    @StringRes
    fun checkError(value: String, validationType: ValidationType): Int? = when {
        validationType.regex.matches(value) -> null
        validationType is ValidationType.Email -> R.string.email_invalid
        validationType is ValidationType.Phone -> R.string.phone_invalid
        validationType is ValidationType.Password -> R.string.weak_password
        else -> null
    }
}

sealed class ValidationType(val regex: Regex) {
    data object Email : ValidationType("""[A-Za-z.0-9_-]+@[A-Za-z.]+\.[A-Za-z]+""".toRegex())
    data object Phone : ValidationType("""(\+7|8) ?\(?\d{3}\)? ?\d{3}[ -]?\d{2}[ -]?\d{2}""".toRegex())
    data object Password : ValidationType("""[a-zA-Z0-9_.!@#$%^&*?]{8,20}""".toRegex())
}