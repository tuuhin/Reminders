package com.eva.reminders.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(controller: NavHostController): T {
    val parent = this.destination.parent?.route ?: return hiltViewModel()
    val viewModelOwner = remember(this) {
        controller.getBackStackEntry(parent)
    }
    SideEffect {
        // Not removing this SideEffect as
        // Help in visualize the routing..
        Log.d("CURRENT_NAV_ROUTE", "${destination.parent?.route}${destination.route}")
    }
    return hiltViewModel(viewModelStoreOwner = viewModelOwner)
}