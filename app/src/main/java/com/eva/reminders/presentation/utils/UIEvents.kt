package com.eva.reminders.presentation.utils

sealed class UIEvents {
    data class ShowSnackBar(val message: String) : UIEvents()
}
