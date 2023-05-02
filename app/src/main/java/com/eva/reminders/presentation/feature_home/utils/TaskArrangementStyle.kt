package com.eva.reminders.presentation.feature_home.utils

enum class TaskArrangementStyle {
    GRID_STYLE,
    BLOCK_STYLE
}

sealed class TaskArrangementEvent {
    object GridStyleEvent : TaskArrangementEvent()
    object BlockStyleEvent : TaskArrangementEvent()
}