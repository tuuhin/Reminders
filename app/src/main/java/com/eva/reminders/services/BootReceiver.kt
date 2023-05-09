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
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    private val receiverTag = "ON_BOOT"

    @Inject
    lateinit var alarmManagerRepo: AlarmManagerRepo

    @Inject
    lateinit var initRepo: BootAlarmInitRepo

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.i(receiverTag, "READY TO GO")
            // Initialize The Alarms
            try {
                CoroutineScope(Dispatchers.IO + SupervisorJob())
                    .launch {
                        initRepo
                            .initializeTasks()
                            .map { async { alarmManagerRepo.createAlarm(it) } }
                            .awaitAll()
                        Log.i(receiverTag, "INITIALIZATION FINISHED")
                    }
            } catch (e: Exception) {
                if (e is CancellationException){
                    throw e
                }
                Log.i(receiverTag, e.message ?: "Failed")
            }
        }
    }
}