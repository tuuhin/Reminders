package com.eva.reminders.presentation.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

fun <T> StateFlow<T>.toMutableStateFlow(scope: CoroutineScope): MutableStateFlow<T> =
    MutableStateFlow(this.value).apply {
        this@toMutableStateFlow
            .onEach { value -> this.value = value }
            .launchIn(scope)
    }
