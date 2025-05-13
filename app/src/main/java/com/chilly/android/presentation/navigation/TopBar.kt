package com.chilly.android.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.chilly.android.di.activity.ActivityScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import timber.log.Timber
import javax.inject.Inject

data class TopBarState(
    @StringRes val titleRes: Int? = null,
    val titleText: String? = null,
    val showBackButton: Boolean = false,
    val actions: List<TopBarAction> = emptyList()
)

data class TopBarAction(
    val icon: ImageVector,
    val event: TopBarEvent
) {
    companion object {
        val Profile = TopBarAction(
            event = TopBarEvent.ProfileClicked,
            icon = Icons.Default.AccountCircle
        )
    }
}

interface TopBarEvent {
    data object BackClicked : TopBarEvent
    data object ProfileClicked : TopBarEvent
}

@ActivityScope
class TopBarEventsRepository @Inject constructor() {
    private val channel =  MutableSharedFlow<TopBarEvent>(extraBufferCapacity = 1)

    val actionsFlow: Flow<TopBarEvent> = channel.asSharedFlow()

    fun dispatch(event: TopBarEvent) {
        Timber.i("dispatching topBar event: $event")
        channel.tryEmit(event)
    }
}

@ActivityScope
class TopBarActionsHandler @Inject constructor(
    private val router: Router,
    private val actionsRepository: TopBarEventsRepository
) {
    fun onEvent(event: TopBarEvent) {
        Timber.i("handling top bar event: $event")
        when(event) {
            TopBarEvent.BackClicked -> router.exit()
            TopBarEvent.ProfileClicked -> router.navigateTo(Destination.Profile)
            else -> actionsRepository.dispatch(event)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChillyTopBar(
    state: TopBarState,
    onEvent: (TopBarEvent) -> Unit
) {
    TopAppBar(
        title = {
            val text = state.titleText
                ?: state.titleRes?.let { stringResource(it) }
                ?: ""
            Text(text = text)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        navigationIcon = {
            if (state.showBackButton) {
                IconButton(
                    onClick = { onEvent(TopBarEvent.BackClicked) }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        },
        actions = {
            state.actions.forEach { topBarAction ->
                IconButton(
                    onClick = { onEvent(topBarAction.event) }
                ) {
                    Icon(
                        imageVector = topBarAction.icon,
                        contentDescription = null
                    )
                }
            }
        }
    )
}
