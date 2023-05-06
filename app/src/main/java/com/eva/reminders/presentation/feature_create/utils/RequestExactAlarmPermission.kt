package com.eva.reminders.presentation.feature_create.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

//Was not sure if I would use this permission so created a simple launcher but wasn't used
@Composable
fun checkExactAlarmPermissions(
    context: Context = LocalContext.current
): Boolean {
    val hasPermission = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.SCHEDULE_EXACT_ALARM
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else mutableStateOf(true)
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasPermission.value = isGranted
        }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !hasPermission.value)
            launcher.launch(Manifest.permission.SCHEDULE_EXACT_ALARM)

    }

    return hasPermission.value
}

