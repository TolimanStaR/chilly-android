package com.chilly.android.presentation.sign_up

import jakarta.inject.Inject
import ru.tinkoff.kotea.core.KoteaStore
import ru.tinkoff.kotea.core.Store


class SignUpStore @Inject constructor(
    commandFlowHandler: SignUpCommandFlowHandler
) : Store<SignUpState, SignUpEvent.UiEvent, SignUpNews> by KoteaStore<SignUpState, SignUpEvent, SignUpEvent.UiEvent, SignUpCommand, SignUpNews>(
    initialState = SignUpState(),
    commandsFlowHandlers = listOf(commandFlowHandler),
    update = SignUpUpdate()
)