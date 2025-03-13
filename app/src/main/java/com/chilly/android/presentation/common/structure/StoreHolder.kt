package com.chilly.android.presentation.common.structure

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelStoreOwner
import io.ktor.http.parameters
import ru.tinkoff.kotea.android.storeViaViewModel
import ru.tinkoff.kotea.core.Store
import kotlin.reflect.KClass

class StoreHolder<S : Store<*, *, *>> (
    factory: () -> S,
    klass: KClass<S>,
    vararg parameters: Any
) {
    private val key: String? = if (parameters.isEmpty())
            klass.simpleName
        else
            "${klass.simpleName}[${parameters.joinToString()}]"

    private val ViewModelStoreOwner._store: S by storeViaViewModel(factory = factory, sharedViewModelKey = key)

    fun getStore(context: Context) : S {
        context as? ComponentActivity ?: throw IllegalStateException()
        return context._store
    }

    companion object {
        inline fun <reified S : Store<*, *, *>> of(
            vararg params: Any,
            noinline factory: () -> S
        ) = StoreHolder(factory, S::class, params)
    }

}