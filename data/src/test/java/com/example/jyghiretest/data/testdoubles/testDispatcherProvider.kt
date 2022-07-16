package com.example.jyghiretest.data.testdoubles

import com.example.jyghiretest.data.network.di.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope

fun TestScope.testDispatcherProvider() = DispatcherProvider(
    io = StandardTestDispatcher(testScheduler),
    main = StandardTestDispatcher(testScheduler)
)

fun CoroutineDispatcher.testDispatcherProvider() = DispatcherProvider(
    io = this,
    main = this
)
