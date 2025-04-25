package com.chilly.android.presentation.common.structure

import android.content.Context
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import ru.tinkoff.kotea.core.Store

@Composable
inline fun <reified S: Store<*, *, *>, C : Any> ScreenHolder(
    noinline componentFactory: @DisallowComposableCalls Context.() -> C,
    noinline storeFactory: @DisallowComposableCalls C.() -> S,
    vararg params: Any,
    crossinline content: @Composable ScreenHolderStoreScope<S, C>.() -> Unit
) {
    val context = LocalContext.current
    val component = remember { context.componentFactory() }
    val holder = remember(component, *params) { StoreHolder.of(params) { component.storeFactory() } }
    val store = holder.getStore(context)

    val scope = remember(store, component) { ScreenHolderStoreScope(store, component) }
    Surface {
        scope.content()
    }
}

class ScreenHolderStoreScope<S: Store<*, *, *>, C: Any> (
    val store: S,
    val component: C
)