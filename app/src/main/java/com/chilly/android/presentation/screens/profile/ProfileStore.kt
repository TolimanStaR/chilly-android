package com.chilly.android.presentation.screens.profile


import ru.tinkoff.kotea.core.KoteaStore
import ru.tinkoff.kotea.core.Store
import javax.inject.Inject

class ProfileStore @Inject constructor(
    commandFlowHandler: ProfileCommandFlowHandler,
    update: ProfileUpdate
) : Store<ProfileState, ProfileEvent, ProfileNews> by KoteaStore(
    initialState = ProfileState(),
    commandsFlowHandlers = listOf(commandFlowHandler),
    update = update
)