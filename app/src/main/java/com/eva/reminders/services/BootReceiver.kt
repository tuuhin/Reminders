package com.eva.reminders.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.eva.reminders.domain.repository.BootAlarmInitRepo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    private val receiverTag = "ON_BOOT"

    private val scope = CoroutineScope(Dispatchers.IO)

    @Inject
    lateinit var alarmManagerRepo: AlarmManagerRepo

    @Inject
    lateinit var initRepo: BootAlarmInitRepo

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        Log.i(receiverTag, "READY TO GO")

        // Initialize The Alarms
        scope.launch {
            try {
                initRepo.initializeTasks()
                    .map { model ->
                        async { alarmManagerRepo.createAlarm(model) }
                    }
                    .awaitAll()
                Log.i(receiverTag, "INITIALIZATION FINISHED")
            } catch (e: Exception) {
                if (e is CancellationException)
                    throw e
                Log.i(receiverTag, e.message ?: "Failed")
            } finally {
                cancel()
                Log.i(receiverTag, "COROUTINE HAS BEEN CANCELED")
            }
        }
    }
}