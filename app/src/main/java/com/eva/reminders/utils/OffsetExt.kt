package com.eva.reminders.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

fun Offset.toDpOffset(): DpOffset = DpOffset(x.dp, y.dp)