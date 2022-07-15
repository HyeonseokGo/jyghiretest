package com.example.jyghiretest

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.launch

fun <T> Flow<T>.safeCollect(
    lifecycleOwner: LifecycleOwner,
    repeatOn: Lifecycle.State = Lifecycle.State.STARTED,
    collectBody: (T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(repeatOn) {
            collect(collectBody)
        }
    }
}

val WhileUiSubscribed = SharingStarted.WhileSubscribed(5_000)