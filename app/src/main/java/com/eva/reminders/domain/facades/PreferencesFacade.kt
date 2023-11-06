package com.eva.reminders.domain.facades

import com.eva.reminders.domain.models.ArrangementStyle
import kotlinx.coroutines.flow.Flow

interface PreferencesFacade {

    val arrangementStyle: Flow<ArrangementStyle>

    suspend fun updateStyle(style: ArrangementStyle)

}