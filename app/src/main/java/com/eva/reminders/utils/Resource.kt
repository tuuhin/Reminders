package com.eva.reminders.utils

sealed class Resource<T> {
    data class Success<T>(val data: T, val message: String? = null) : Resource<T>()
    data class Error<T>(val data: T? = null, val message: String) : Resource<T>()
    class Loading<T>() : Resource<T>()
}
