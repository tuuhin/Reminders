package com.eva.reminders.presentation.utils

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import com.eva.reminders.domain.models.ArrangementStyle

val LocalArrangementStyle = compositionLocalOf {ArrangementStyle.BLOCK_STYLE }

val LocalSnackBarHostProvider = compositionLocalOf { SnackbarHostState() }