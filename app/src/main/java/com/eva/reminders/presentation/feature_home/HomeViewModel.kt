package com.eva.reminders.presentation.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.domain.repository.TaskRepository
import com.eva.reminders.presentation.utils.HomeTabs
import com.eva.reminders.presentation.utils.UIEvents
import com.eva.reminders.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UIEvents>()

    private val _currentTab = MutableStateFlow<HomeTabs>(HomeTabs.AllReminders)
    val currentTab = _currentTab.asStateFlow()

    private val _tasks = MutableStateFlow(emptyList<TaskModel>())
    val tasks = _tasks.asStateFlow()

    init {
        getAllTasks()
    }


    fun changeCurrentTab(tab: HomeTabs) {
        _currentTab.update { tab }
    }


    private fun getAllTasks() {
        viewModelScope.launch {
            taskRepository
                .getAllTasks()
                .onEach { res ->
                    when (res) {
                        is Resource.Error -> _uiEvent.emit(UIEvents.ShowSnackBar(res.message))
                        is Resource.Loading -> {}
                        is Resource.Success -> _tasks.update { res.data }
                    }
                }
                .launchIn(this)
        }
    }

}