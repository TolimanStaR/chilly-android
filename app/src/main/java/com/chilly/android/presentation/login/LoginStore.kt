package com.chilly.android.presentation.login

import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.tinkoff.kotea.core.KoteaStore
import ru.tinkoff.kotea.core.Store

class LoginStore @AssistedInject constructor(
    commandFlowHandler: LoginCommandFlowHandler
) : Store<LoginState, LoginEvent.UiEvent, LoginNews> by KoteaStore(
    initialState = LoginState(),
    commandsFlowHandlers = listOf(commandFlowHandler),
    update = LoginUpdate()
)  {

    @AssistedFactory
    interface Factory {
        fun build() : LoginStore
    }
}



