package com.eva.reminders.presentation.utils

data class ShowContent<T>(
    val isLoading: Boolean = true,
    val content: T
)
