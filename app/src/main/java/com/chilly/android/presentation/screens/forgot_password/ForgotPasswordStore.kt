package com.chilly.android.presentation.screens.forgot_password

import ru.tinkoff.kotea.core.KoteaStore
import ru.tinkoff.kotea.core.Store
import javax.inject.Inject

class ForgotPasswordStore @Inject constructor(
    commandFlowHandler: ForgotPasswordCommandFlowHandler,
    update: ForgotPasswordUpdate
) : Store<ForgotPasswordState, ForgotPasswordEvent, ForgotPasswordNews> by KoteaStore(
    initialState = ForgotPasswordState(),
    commandsFlowHandlers = listOf(commandFlowHandler),
    update = update
)