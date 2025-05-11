package com.chilly.android.presentation.screens.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import com.chilly.android.R
import com.chilly.android.activityComponent
import com.chilly.android.di.screens.DaggerHistoryComponent
import com.chilly.android.di.screens.HistoryComponent
import com.chilly.android.presentation.common.components.ChillyButton
import com.chilly.android.presentation.common.components.ChillyButtonType
import com.chilly.android.presentation.common.components.PlaceListItem
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.common.structure.collectState
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.navigation.fadingComposable
import com.chilly.android.presentation.screens.history.HistoryEvent.UiEvent
import com.chilly.android.presentation.theme.ChillyTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryScreen(
    state: HistoryUiState,
    padding: PaddingValues,
    onEvent: (UiEvent) -> Unit
) {
    var clearDialogIsShown by remember { mutableStateOf(false) }
    // Delete All dialog
    if (clearDialogIsShown) {
        AlertDialog(
            onDismissRequest = {
                clearDialogIsShown = false
            },
            confirmButton = {
                ChillyButton(
                    textRes = R.string.confirm_button,
                    onClick = {
                        onEvent(UiEvent.ClearAllConfirmed)
                        clearDialogIsShown = false
                    }
                )
            },
            dismissButton = {
                ChillyButton(
                    textRes = R.string.cancel_button,
                    onClick = {
                        clearDialogIsShown = false
                    },
                    type = ChillyButtonType.Tertiary
                )
            },
            title = {
                Text(stringResource(R.string.clear_history_dialog_title))
            },
            text = {
                Text(stringResource(R.string.clear_history_dialog_text))
            },
            icon = {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.history_screen_title))
                },
                actions = {
                    IconButton(
                        enabled = state.historyItems.isNotEmpty(),
                        onClick = {
                            clearDialogIsShown = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null
                        )
                    }
                    IconButton(
                        onClick = {
                            onEvent(UiEvent.ProfileClicked)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { scaffoldPadding ->
        if (state.historyItems.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(stringResource(R.string.empty_history_text))
            }
            return@Scaffold
        }
        val mergedPadding = remember(padding, scaffoldPadding) {
            PaddingValues(
                top = maxOf(padding.calculateTopPadding(), scaffoldPadding.calculateTopPadding()),
                bottom = maxOf(padding.calculateBottomPadding(), scaffoldPadding.calculateBottomPadding())
            )
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(mergedPadding)
                .padding(16.dp)
        ) {
            items(
                state.historyItems,
                key = {
                    when(it) {
                        is HistoryListItem.PlaceItem -> it.item.id
                        is HistoryListItem.HistoryDateLabel -> it.value
                    }
                }
            ) { historyItem ->
                when(historyItem) {
                    is HistoryListItem.HistoryDateLabel -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            HorizontalDivider(Modifier.weight(1f))
                            Text(
                                text = historyItem.value,
                                modifier = Modifier.padding(8.dp)
                            )
                            HorizontalDivider(Modifier.weight(1f))
                        }
                    }
                    is HistoryListItem.PlaceItem -> {
                        SwipeToDismissItem(
                            item = historyItem.item,
                            onRemove = {
                                onEvent(UiEvent.ItemSwipedToDelete(it))
                            }
                        ) {
                            PlaceListItem(it.place) {
                                onEvent(UiEvent.ItemClicked(it.place.id))
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

fun NavGraphBuilder.installHistoryScreen(padding: PaddingValues) {
    fadingComposable<Destination.History> {
        ScreenHolder<HistoryStore, HistoryComponent>(
            componentFactory = {
                DaggerHistoryComponent.builder()
                    .appComponent(activityComponent)
                    .build()
            },
            storeFactory = { store() }
        ) {
            val state = collectState(component.uiStateMapper)
            NewsCollector(component.newsCollector)
            HistoryScreen(state.value, padding, store::dispatch)
        }
    }
}

@Composable
private fun <T: Any> SwipeToDismissItem(
    item: T,
    onRemove: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit,
) {
    var isRemoved by remember { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when(value) {
                SwipeToDismissBoxValue.EndToStart -> {
                    isRemoved = true
                    true
                }
                else -> false
            }
        },
        positionalThreshold = { it * 0.3f }
    )

    LaunchedEffect(isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onRemove(item)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(animationDuration)
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = {
                DismissBoxBackground(dismissState)
            },
            enableDismissFromStartToEnd = false,
        ) {
            content(item)
        }
    }
}

@Composable
private fun DismissBoxBackground(dismissState: SwipeToDismissBoxState) {
    val color = when(dismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.primary
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color, RoundedCornerShape(16.dp))
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(Alignment.CenterEnd)
                .padding(end = 8.dp)
        )
    }
}

@Composable
@PreviewLightDark
@Preview(name = "HistoryScreen", showSystemUi = true, showBackground = true)
private fun PreviewHistoryScreen() {
    ChillyTheme {
        HistoryScreen(
            state = HistoryUiState(),
            padding = PaddingValues(),
            onEvent = {}
        )
    }
}

