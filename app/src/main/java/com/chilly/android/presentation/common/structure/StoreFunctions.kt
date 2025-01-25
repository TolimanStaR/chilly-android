package com.chilly.android.presentation.common.structure

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import ru.tinkoff.kotea.core.Store

@Composable
inline fun <reified S: Store<*, *, *>> ScreenHolder(
    noinline storeFactory: Context.() -> S,
    content: @Composable S.() -> Unit
) {
    val context = LocalContext.current
    val holder = StoreHolder { context.storeFactory() }
    val store = holder.getStore(context)

    store.content()
}

@Composable
inline fun <reified S: Store<*, *, *>, C : Any> ScreenHolder(
    noinline componentFactory: @DisallowComposableCalls Context.() -> C,
    noinline storeFactory: @DisallowComposableCalls C.() -> S,
    content: @Composable ScreenHolderStoreScope<S, C>.() -> Unit
) {
    val context = LocalContext.current
    val component = remember { context.componentFactory() }
    val holder = remember(component) { StoreHolder { component.storeFactory() } }
    val store = holder.getStore(context)

    val scope = remember(store, component) { ScreenHolderStoreScope.of(store, component) }
    scope.content()
}

interface ScreenHolderStoreScope<S: Store<*, *, *>, C: Any> {
    val store: S
    val component: C

    companion object {
        fun <S: Store<*, *, *>, C: Any> of(store: S, component: C) = object : ScreenHolderStoreScope<S, C> {
            override val store = store
            override val component = component
        }
    }
}


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