package com.eva.reminders.data.preferences

import com.eva.reminders.domain.facades.PreferencesFacade
import com.eva.reminders.domain.models.ArrangementStyle
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.RENDEZVOUS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow

class PreferencesFacadeTestImpl : PreferencesFacade {

    /** AS here during a test we don't need the Buffer thus capacity set to [RENDEZVOUS]
     */
    private val _channel = Channel<ArrangementStyle>(capacity = RENDEZVOUS)

    override val arrangementStyle: Flow<ArrangementStyle>
        get() = _channel.consumeAsFlow()

    override suspend fun updateStyle(style: ArrangementStyle) = _channel.send(style)

    fun cancelChannel() = _channel.cancel()

}