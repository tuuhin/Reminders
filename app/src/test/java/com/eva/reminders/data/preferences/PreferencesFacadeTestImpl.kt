package com.eva.reminders.data.preferences

import com.eva.reminders.domain.facades.PreferencesFacade
import com.eva.reminders.domain.models.ArrangementStyle
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow

class PreferencesFacadeTestImpl : PreferencesFacade {

    private val _channel = Channel<ArrangementStyle>()

    override val arrangementStyle: Flow<ArrangementStyle>
        get() = _channel.consumeAsFlow()

    override suspend fun updateStyle(style: ArrangementStyle) = _channel.send(style)

    fun cancelChannel() = _channel.cancel()

}