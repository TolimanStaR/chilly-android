package com.chilly.android.presentation.common.structure

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelStoreOwner
import ru.tinkoff.kotea.android.storeViaViewModel
import ru.tinkoff.kotea.core.Store
import timber.log.Timber
import kotlin.reflect.KClass

class StoreHolder<S : Store<*, *, *>> (
    activity: ComponentActivity,
    factory: () -> S,
    klass: KClass<S>,
    vararg parameters: Any
): ViewModelStoreOwner by activity {

    private val key: String? =
        if (parameters.isNotEmpty()) "${klass.simpleName}[${parameters.joinToString()}]"
        else klass.simpleName

    val store: S by storeViaViewModel(
        factory = {
            Timber.d("creating store: key=$key")
            factory.invoke()
        },
        sharedViewModelKey = key
    )

    companion object {
        inline fun <reified S : Store<*, *, *>> of(
            activity: ComponentActivity,
            vararg params: Any,
            noinline factory: () -> S
        ) = StoreHolder(activity, factory, S::class, *params)
    }
}