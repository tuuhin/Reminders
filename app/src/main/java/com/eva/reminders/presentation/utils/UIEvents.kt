package com.eva.reminders.presentation.utils

sealed interface UIEvents {
    data class ShowSnackBar(val message: String) : UIEvents
    data object NavigateBack : UIEvents
}
