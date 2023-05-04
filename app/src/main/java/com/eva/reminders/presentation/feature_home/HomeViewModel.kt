package com.eva.reminders.presentation.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.domain.repository.TaskRepository
import com.eva.reminders.presentation.feature_home.utils.TaskArrangementEvent
import com.eva.reminders.presentation.feature_home.utils.TaskArrangementStyle
import com.eva.reminders.presentation.utils.HomeTabs
import com.eva.reminders.presentation.utils.ShowContent
import com.eva.reminders.presentation.utils.UIEvents
import com.eva.reminders.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UIEvents>()
    val uiEvents = _uiEvent.asSharedFlow()

    private val _currentTab = MutableStateFlow<HomeTabs>(HomeTabs.AllReminders)
    val currentTab = _currentTab.asStateFlow()

    private val _tasks =
        MutableStateFlow<ShowContent<List<TaskModel>>>(ShowContent(content = emptyList()))


    val tasks = combine(_currentTab, _tasks) { tabs, tasks ->
        when (tabs) {
            HomeTabs.AllReminders -> tasks.copy(content = tasks.content
                .filter { !it.isArchived })

            HomeTabs.Archived -> tasks.copy(content = tasks.content
                .filter { it.isArchived })

            HomeTabs.NonScheduled -> tasks.copy(content = tasks.content
                .filter { !it.isArchived && it.reminderAt.at == null })

            HomeTabs.Scheduled -> tasks.copy(content = tasks.content
                .filter { !it.isArchived && it.reminderAt.at != null })
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(500L),
        ShowContent(content = emptyList())
    )


    private val _arrangement = MutableStateFlow(TaskArrangementStyle.BLOCK_STYLE)
    val arrangement = _arrangement.asStateFlow()

    init {
        getAllTasks()
    }


    fun onArrangementChange(event: TaskArrangementEvent) {
        when (event) {
            TaskArrangementEvent.BlockStyleEvent -> _arrangement
                .update { TaskArrangementStyle.BLOCK_STYLE }

            TaskArrangementEvent.GridStyleEvent -> _arrangement
                .update { TaskArrangementStyle.GRID_STYLE }
        }
    }


    fun changeCurrentTab(tab: HomeTabs) {
        _currentTab.update { tab }
    }


    private fun getAllTasks() {
        viewModelScope.launch {
            taskRepository.getAllTasks().onEach { res ->
                delay(2.seconds)
                when (res) {
                    is Resource.Error -> {
                        _uiEvent.emit(UIEvents.ShowSnackBar(res.message))
                        _tasks.update { it.copy(isLoading = false, content = emptyList()) }
                    }

                    is Resource.Loading -> _tasks.update { it.copy(isLoading = true) }
                    is Resource.Success -> _tasks.update {
                        it.copy(
                            isLoading = false,
                            content = res.data
                        )
                    }
                }
            }.launchIn(this)
        }
    }

}