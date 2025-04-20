package com.chilly.android.presentation.common.structure

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.tinkoff.kotea.core.Store

@Composable
fun <N: Any, S: Store<*, *, N>> S.NewsCollector(
    collector: FlowCollector<N>
) {
    news.Collector(collector)
}

@Composable
fun <N: Any, S: Store<*, *, N>> ScreenHolderStoreScope<S, *>.NewsCollector(
    collector: FlowCollector<N>
) {
    store.NewsCollector(collector)
}

@Composable
fun <St: Any, S: Store<St, *, *>> ScreenHolderStoreScope<S, *>.collectState(): State<St> = store.state.collectAsStateWithLifecycle()

@Composable
fun <St: Any, UiSt: Any, S: Store<St, *, *>> ScreenHolderStoreScope<S, *>.collectState(
    mapper: UiStateMapper<St, UiSt>
): State<UiSt> {
    val resources = LocalContext.current.resources
    val initialValue = runBlocking { mapToUi(resources, store.state.value, mapper) }
    return store.state
        .map { value -> mapToUi(resources, value, mapper) }
        .collectAsStateWithLifecycle(initialValue)
}

private suspend fun <St: Any, UiSt: Any> mapToUi(resources: Resources, state: St, mapper: UiStateMapper<St, UiSt>): UiSt {
    return withContext(Dispatchers.Default) {
        with(mapper) {
            resources.mapToUiState(state)
        }
    }
}

@Composable
fun <E: Any, VM: ViewModelWithEffects<E, *>> VM.EffectCollector(
    collector: FlowCollector<E>
) {
    effects.Collector(collector)
}

@Composable
fun <E: Any, VM: ViewModelWithEffects<E, *>> ScreenHolderViewModelScope<VM, *>.EffectCollector(
    collector: FlowCollector<E>
) {
    viewModel.EffectCollector(collector)
}

@Composable
private fun <T: Any> Flow<T>.Collector(
    collector: FlowCollector<T>
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(true) {
        flowWithLifecycle(lifecycle)
            .collect(collector)
    }
}