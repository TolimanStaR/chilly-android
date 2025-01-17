package com.chilly.android.presentation.common.structure

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelStoreOwner
import ru.tinkoff.kotea.android.storeViaViewModel
import ru.tinkoff.kotea.core.Store

class StoreHolder<S : Store<*, *, *>>(
    factory: () -> S,
) {

    private val ViewModelStoreOwner._store: S by storeViaViewModel(factory = factory)

    fun getStore(context: Context) : S {
        context as? ComponentActivity ?: throw IllegalStateException()
        return context._store
    }

}