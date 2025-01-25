package com.chilly.android.presentation.sign_up

import kotlinx.coroutines.flow.Flow
import ru.tinkoff.kotea.core.CommandsFlowHandler
import javax.inject.Inject

class SignUpCommandFlowHandler @Inject constructor(
) : CommandsFlowHandler<SignUpCommand, SignUpEvent.CommandEvent> {

    override fun handle(commands: Flow<SignUpCommand>): Flow<SignUpEvent.CommandEvent> {
        TODO("Not yet implemented")
    }
}