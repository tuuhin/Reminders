package com.eva.reminders.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.eva.reminders.domain.repository.BootAlarmInitRepo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    private val receiverTag = "ON_BOOT"

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @Inject
    lateinit var alarmManagerRepo: AlarmManagerRepo

    @Inject
    lateinit var initRepo: BootAlarmInitRepo

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        Log.i(receiverTag, "READY TO SET UP THE ALARMS")

        // Initialize The Alarms after the boot
        scope.launch {
            try {
                val tasks = initRepo.initializeTasks()
                tasks.forEach(alarmManagerRepo::createAlarm)

                Log.i(receiverTag, "INITIALIZATION FINISHED")
            } catch (e: Exception) {
                Log.i(receiverTag, e.message ?: "FAILED TO INITIALIZE THE TASKS")
            } finally {
                cancel()
                Log.i(receiverTag, "COROUTINE HAS BEEN CANCELED")
            }
        }
    }
}