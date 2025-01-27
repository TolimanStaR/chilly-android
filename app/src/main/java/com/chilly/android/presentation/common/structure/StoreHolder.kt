package com.chilly.android.presentation.common.structure

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelStoreOwner
import ru.tinkoff.kotea.android.storeViaViewModel
import ru.tinkoff.kotea.core.Store
import kotlin.reflect.KClass

class StoreHolder<S : Store<*, *, *>> (
    factory: () -> S,
    klass: KClass<S>
) {

    private val ViewModelStoreOwner._store: S by storeViaViewModel(factory = factory, sharedViewModelKey = klass.simpleName)

    fun getStore(context: Context) : S {
        context as? ComponentActivity ?: throw IllegalStateException()
        return context._store
    }

    companion object {
        inline fun <reified S : Store<*, *, *>> of(
            noinline factory: () -> S
        ) = StoreHolder(factory, S::class)
    }

}