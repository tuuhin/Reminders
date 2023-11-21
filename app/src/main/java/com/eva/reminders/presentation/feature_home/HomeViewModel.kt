package com.eva.reminders.presentation.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.reminders.domain.facades.PreferencesFacade
import com.eva.reminders.domain.models.ArrangementStyle
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.domain.repository.TaskRepository
import com.eva.reminders.presentation.feature_home.utils.HomeSearchBarEvents
import com.eva.reminders.presentation.feature_home.utils.HomeSearchBarState
import com.eva.reminders.presentation.feature_home.utils.HomeTaskPresenter
import com.eva.reminders.presentation.feature_home.utils.SearchResultsType
import com.eva.reminders.presentation.feature_home.utils.SearchType
import com.eva.reminders.presentation.utils.HomeTabs
import com.eva.reminders.presentation.utils.ShowContent
import com.eva.reminders.presentation.utils.UIEvents
import com.eva.reminders.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val preference: PreferencesFacade,
    private val presenter: HomeTaskPresenter
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UIEvents>()
    val uiEvents = _uiEvent.asSharedFlow()

    private val _currentTab = MutableStateFlow<HomeTabs>(HomeTabs.AllReminders)
    val currentTab = _currentTab.asStateFlow()

    private val _tasks =
        MutableStateFlow<ShowContent<List<TaskModel>>>(ShowContent(content = emptyList()))

    val tasks = combine(_currentTab, _tasks) { tabs, tasks ->
        presenter.showTaskByTabs(tabs, tasks)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(500L),
        ShowContent(content = emptyList())
    )


    private val _searchType = MutableStateFlow<SearchType>(SearchType.BlankSearch)

    val searchedTasks = combine(_tasks, _searchType, _currentTab) { tasks, type, tab ->
        presenter.searchResultsType(searchType = type, tabs = tab, tasks = tasks.content)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(500L),
        SearchResultsType.NoResultsType
    )

    private val _homeSearchBarState = MutableStateFlow(HomeSearchBarState())
    val homeSearchBarState = _homeSearchBarState.asStateFlow()

    val colorOptions = tasks.map { showContent ->
        presenter.colorOptions(showContent.content)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(500L),
        emptyList()
    )

    val arrangement = preference.arrangementStyle
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            initialValue = ArrangementStyle.GRID_STYLE
        )

    init {
        getAllTasks()
    }

    fun onSearchBarEvents(events: HomeSearchBarEvents) {
        when (events) {
            is HomeSearchBarEvents.OnActiveChange -> {
                _homeSearchBarState
                    .update { it.copy(isActive = events.active, query = "") }
                _searchType.update { SearchType.BlankSearch }
            }

            is HomeSearchBarEvents.OnQueryChange -> _homeSearchBarState
                .update { it.copy(query = events.query) }

            is HomeSearchBarEvents.OnSearch -> {
                if (events.search.isEmpty())
                    _searchType.update { SearchType.BlankSearch }
                else _searchType.update { SearchType.BasicSearch(events.search) }
            }
        }
    }

    fun onSearchType(type: SearchType): Unit = _searchType.update { type }

    fun onArrangementChange(style: ArrangementStyle) {
        viewModelScope.launch(Dispatchers.IO) {
            preference.updateStyle(style)
        }
    }

    fun changeCurrentTab(tab: HomeTabs): Unit = _currentTab.update { tab }

    private fun getAllTasks() {
        viewModelScope.launch {
            taskRepository.getAllTasks()
                .onEach { res ->
                    when (res) {
                        is Resource.Error -> {
                            _uiEvent.emit(UIEvents.ShowSnackBar(res.message))
                            _tasks.update { content ->
                                content.copy(
                                    isLoading = false,
                                    content = emptyList()
                                )
                            }
                        }

                        is Resource.Success -> _tasks.update { content ->
                            content.copy(isLoading = false, content = res.data)
                        }

                        Resource.Loading -> _tasks.update { content -> content.copy(isLoading = true) }
                    }
                }
                .launchIn(this)
        }
    }
}