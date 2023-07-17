package com.eva.reminders.presentation.feature_home.utils

data class HomeSearchBarState(
    val query: String = "",
    val isActive: Boolean = false
)

sealed interface HomeSearchBarEvents {
    data class OnQueryChange(val query: String) : HomeSearchBarEvents
    data class OnActiveChange(val active: Boolean) : HomeSearchBarEvents
    data class OnSearch(val search: String) : HomeSearchBarEvents
}
