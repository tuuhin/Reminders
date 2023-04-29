package com.eva.reminders.presentation.feature_create.utils

enum class ReminderFrequency(val isRepeating: Boolean) {
    DO_NOT_REPEAT(false),
    DAILY(true),
}