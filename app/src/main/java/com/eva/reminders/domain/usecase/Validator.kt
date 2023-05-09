package com.eva.reminders.domain.usecase

data class Validator(
    val isValid: Boolean = false,
    val error: String? = null
)
