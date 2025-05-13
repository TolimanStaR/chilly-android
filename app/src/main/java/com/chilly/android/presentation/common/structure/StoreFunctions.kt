package com.chilly.android.presentation.common.structure

import android.content.Context
import androidx.activity.ComponentActivity
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
    val activity = LocalContext.current as? ComponentActivity
        ?: throw IllegalStateException("ScreenHolder requires to be installed into ComponentActivity subclass")

    val component = remember { activity.componentFactory() }
    val holder = remember(component, *params) {
        StoreHolder.of(activity, *params) {
            storeFactory.invoke(component)
        }
    }

    Surface {
        val scope = remember(holder.store, component) {
            ScreenHolderStoreScope(holder.store, component)
        }
        scope.content()
    }
}

class ScreenHolderStoreScope<S: Store<*, *, *>, C: Any> (
    val store: S,
    val component: C
)