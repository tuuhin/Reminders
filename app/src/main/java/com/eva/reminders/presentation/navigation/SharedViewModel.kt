package com.eva.reminders.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(controller: NavHostController): T {
    val parent = this.destination.parent?.route ?: return hiltViewModel()
    Log.i("PARENT", parent)
    val viewModelOwner = remember(this) {
        controller.getBackStackEntry(parent)
    }
    return hiltViewModel(viewModelStoreOwner = viewModelOwner)
}