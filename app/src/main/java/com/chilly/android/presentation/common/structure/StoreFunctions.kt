package com.chilly.android.presentation.common.structure

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
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
fun <N: Any, S: Store<*, *, N>> S.NewsCollector(
    collector: FlowCollector<N>
) {
    news.Collector(collector)
}

@Composable
fun <E: Any, VM: ViewModelWithEffects<E, *>> VM.EffectCollector(
    collector: FlowCollector<E>
) {
    effects.Collector(collector)
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