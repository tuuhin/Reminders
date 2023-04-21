package com.eva.reminders.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.eva.reminders.domain.repository.BootAlarmInitRepo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmManagerRepo: AlarmManagerRepo

    @Inject
    lateinit var initRepo: BootAlarmInitRepo

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            println("Booted")
            // Initialize The Alarms
            initRepo.initializeTasks().map(alarmManagerRepo::createAlarm)
        }
    }
}