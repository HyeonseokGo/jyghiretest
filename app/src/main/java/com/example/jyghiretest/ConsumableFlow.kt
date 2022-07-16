package com.example.jyghiretest

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface Consumable

interface ConsumableFlow<T>: Flow<T> {
    fun consume()
}


class ConsumableFlowImpl<T>(
    private val flow: Flow<T>
) : ConsumableFlow<T> {

    var isConsumed = false

    override suspend fun collect(collector: FlowCollector<T>) {
        flow.collect {
            if (!isConsumed) collector.emit(it)
        }
    }

    override fun consume() {
        isConsumed = true
    }

}


fun <T> Flow<T>.asConsumableFlow(): ConsumableFlow<T> {
    return ConsumableFlowImpl(this)
}


fun <T> ConsumableFlow<T>.safeConsumeCollect(
    lifecycleOwner: LifecycleOwner,
    repeatOn: Lifecycle.State = Lifecycle.State.STARTED,
    collectBody: (T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(repeatOn) {
            collect {
                consume()
                collectBody(it)
            }
        }
    }
}
