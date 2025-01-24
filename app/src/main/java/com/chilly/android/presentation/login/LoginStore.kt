package com.chilly.android.presentation.login

import ru.tinkoff.kotea.core.KoteaStore
import ru.tinkoff.kotea.core.Store
import javax.inject.Inject

class LoginStore @Inject constructor(
    commandFlowHandler: LoginCommandFlowHandler
) : Store<LoginState, LoginEvent.UiEvent, LoginNews> by KoteaStore(
    initialState = LoginState(),
    commandsFlowHandlers = listOf(commandFlowHandler),
    update = LoginUpdate()
)



