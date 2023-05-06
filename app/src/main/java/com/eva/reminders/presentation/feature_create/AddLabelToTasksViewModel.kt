package com.eva.reminders.presentation.feature_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.presentation.feature_labels.utils.SelectLabelState
import com.eva.reminders.presentation.utils.UIEvents
import com.eva.reminders.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddLabelToTasksViewModel(
    private val labelRepo: TaskLabelsRepository
) : ViewModel() {

    private val _uiEvents = MutableSharedFlow<UIEvents>()

    private val _queriedLabels = MutableStateFlow(emptyList<TaskLabelModel>())

    private val _selectedLabelFlow = MutableStateFlow(emptyList<SelectLabelState>())
    val selectedLabelsAsFlow = _selectedLabelFlow.asStateFlow()

    val labelSelector = combine(_queriedLabels, _selectedLabelFlow) { query, selected ->
        query.map { model ->
            val isSelected = selected.any { state -> state.idx == model.id }
            SelectLabelState(idx = model.id, label = model.label, isSelected = isSelected)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(500L), emptyList())


    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()


    private var searchJob: Job? = null

    // Set the labels when its an update request
    fun setSelectedLabels(labels: List<TaskLabelModel>) {
        _selectedLabelFlow.update { emptyList() }
        _selectedLabelFlow.update {
            labels.map { SelectLabelState(it.id, it.label, isSelected = true) }
        }
    }

    // Don't seems necessary but during a create route all labels are cleared
    fun clearAllLabels() = _selectedLabelFlow.update { emptyList() }

    init {
        searchLabels()
    }

    fun onSelect(state: SelectLabelState) {
        if (state.isSelected) _selectedLabelFlow.update { states ->
            states.filter { it.idx != state.idx }
        }
        else _selectedLabelFlow.update { states ->
            buildList {
                addAll(states)
                add(state)
            }
        }
    }

    fun searchLabels(search: String = "") {
        _query.update { search }
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            labelRepo.searchLabels(search).cancellable().onEach { models ->
                    _queriedLabels.update { models }
                }.launchIn(this)
        }
    }

    fun createLabel() {
        viewModelScope.launch(Dispatchers.IO) {
            if (query.value.isNotEmpty()) {
                val trimmedLabel = _query.value.trim()
                when (val res = labelRepo.createLabel(trimmedLabel)) {
                    is Resource.Error -> _uiEvents.emit(UIEvents.ShowSnackBar(res.message))

                    is Resource.Loading -> {}
                    is Resource.Success -> _uiEvents.emit(UIEvents.ShowSnackBar("Added new label ${_query.value}"))
                }
            } else _uiEvents.emit(UIEvents.ShowSnackBar("Cannot create a blank a label"))
        }
    }
}