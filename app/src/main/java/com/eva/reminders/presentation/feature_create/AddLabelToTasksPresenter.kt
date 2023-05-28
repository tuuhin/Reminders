package com.eva.reminders.presentation.feature_create

import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.presentation.feature_labels.utils.SelectLabelState
import com.eva.reminders.presentation.utils.UIEvents
import com.eva.reminders.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
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

class AddLabelToTasksPresenter(
    private val labelRepo: TaskLabelsRepository,
    private val dispatcher: CoroutineDispatcher,
    private val coroutineScope: CoroutineScope
) {

    private val _uiEvents = MutableSharedFlow<UIEvents>()

    private val _queriedLabels = MutableStateFlow(emptyList<TaskLabelModel>())

    private val _selectedLabelFlow = MutableStateFlow(emptyList<SelectLabelState>())
    val selectedLabelsAsFlow = _selectedLabelFlow.asStateFlow()

    val labelSelector = combine(_queriedLabels, _selectedLabelFlow) { query, selected ->
        query.map { model ->
            val isSelected = selected.any { state -> state.idx == model.id }
            SelectLabelState(idx = model.id, label = model.label, isSelected = isSelected)
        }
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(5000L), emptyList())


    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()


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

    private var searchJob: Job? = null

    fun searchLabels(search: String = "") {
        _query.update { search }
        searchJob?.cancel()
        searchJob = coroutineScope.launch(dispatcher) {
            labelRepo.searchLabels(search).cancellable().onEach { models ->
                _queriedLabels.update { models }
            }.launchIn(this)
        }
    }

    fun createLabel() {
        coroutineScope.launch(dispatcher) {
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