package com.eva.reminders.presentation.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.domain.repository.TaskRepository
import com.eva.reminders.presentation.feature_home.utils.SearchResultsType
import com.eva.reminders.presentation.feature_home.utils.SearchType
import com.eva.reminders.presentation.feature_home.utils.TaskArrangementEvent
import com.eva.reminders.presentation.feature_home.utils.TaskArrangementStyle
import com.eva.reminders.presentation.utils.HomeTabs
import com.eva.reminders.presentation.utils.SaveUserArrangementPreference
import com.eva.reminders.presentation.utils.ShowContent
import com.eva.reminders.presentation.utils.UIEvents
import com.eva.reminders.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val preference: SaveUserArrangementPreference
) : ViewModel() {

    val arrangement = preference.readArrangementStyle.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        initialValue = TaskArrangementStyle.BLOCK_STYLE
    )

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

    val colorOptions = tasks.map {
        it.content.map { model -> model.color }.distinct()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(500L),
        emptyList()
    )


    private val _searchType = MutableStateFlow<SearchType>(SearchType.BlankSearch)


    val searchedTasks = combine(_tasks, _searchType) { tasks, type ->
        when (type) {
            is SearchType.BasicSearch ->
                SearchResultsType.SearchResults(
                    tasks.content
                        .filter {
                            val colorFilter =
                                it.color.name.lowercase() == type.query.trim().lowercase()
                            val labelFilter =
                                it.labels.map { label -> label.label.trim().lowercase() }
                                    .contains(type.query.lowercase())
                            colorFilter || labelFilter
                        }
                )

            SearchType.BlankSearch -> SearchResultsType.NoResultsType
            is SearchType.ColorSearch ->
                SearchResultsType.SearchResults(
                    tasks.content.filter { it.color == type.search }
                )

            is SearchType.LabelSearch ->
                SearchResultsType.SearchResults(
                    tasks.content.filter { it.labels.contains(type.labelModel) }
                )
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(500L),
        SearchResultsType.NoResultsType
    )


    init {
        getAllTasks()
    }

    fun onSearchType(type: SearchType): Unit = _searchType.update { type }

    fun onArrangementChange(event: TaskArrangementEvent) {
        viewModelScope.launch {

            when (event) {
                TaskArrangementEvent.BlockStyleEvent -> preference.updateArrangementStyle(
                    TaskArrangementStyle.BLOCK_STYLE
                )
                TaskArrangementEvent.GridStyleEvent -> preference.updateArrangementStyle(
                    TaskArrangementStyle.GRID_STYLE
                )
            }
        }
    }

    fun changeCurrentTab(tab: HomeTabs): Unit = _currentTab.update { tab }

    private fun getAllTasks() {
        viewModelScope.launch {
            taskRepository.getAllTasks().onEach { res ->
                when (res) {
                    is Resource.Error -> {
                        _uiEvent.emit(UIEvents.ShowSnackBar(res.message))
                        _tasks.update { it.copy(isLoading = false, content = emptyList()) }
                    }

                    is Resource.Loading -> _tasks.update { it.copy(isLoading = true) }
                    is Resource.Success -> _tasks.update {
                        it.copy(isLoading = false, content = res.data)
                    }
                }
            }.launchIn(this)
        }
    }

}