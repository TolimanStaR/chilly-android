package com.chilly.android.presentation.common.structure

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
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
inline fun <reified N: Any, S: Store<*, *, N>> S.NewsCollector(
    collector: FlowCollector<N>
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(true) {
        news.flowWithLifecycle(lifecycle)
            .collect(collector)
    }
}