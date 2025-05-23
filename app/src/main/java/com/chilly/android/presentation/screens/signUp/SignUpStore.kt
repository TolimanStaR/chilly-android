package com.chilly.android.presentation.screens.signUp

import jakarta.inject.Inject
import ru.tinkoff.kotea.core.KoteaStore
import ru.tinkoff.kotea.core.Store


class SignUpStore @Inject constructor(
    commandFlowHandler: SignUpCommandFlowHandler,
    update: SignUpUpdate,
) : Store<SignUpState, SignUpEvent.UiEvent, SignUpNews> by KoteaStore(
    initialState = SignUpState(),
    commandsFlowHandlers = listOf(commandFlowHandler),
    update = update
)