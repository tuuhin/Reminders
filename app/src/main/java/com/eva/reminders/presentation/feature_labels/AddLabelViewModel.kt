package com.eva.reminders.presentation.feature_labels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.presentation.feature_labels.utils.SelectLabelState
import com.eva.reminders.presentation.feature_labels.utils.SelectLabelsEvents
import com.eva.reminders.presentation.utils.UIEvents
import com.eva.reminders.presentation.utils.toMutableStateFlow
import com.eva.reminders.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddLabelViewModel @Inject constructor(
    private val labelRepo: TaskLabelsRepository
) : ViewModel() {

    private val _uiEvents = MutableSharedFlow<UIEvents>()
    val uiEvents = _uiEvents.asSharedFlow()

    private val _queriedLabels = MutableStateFlow(emptyList<TaskLabelModel>())

    private val _selectedRef = mutableListOf<SelectLabelState>()

    private val _labelSelector = _queriedLabels.map { labels ->
        labels.map {
            val isSelected = _selectedRef.any { state -> it.id == state.idx }
            SelectLabelState(idx = it.id, label = it.label, isSelected = isSelected)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(500L), emptyList())
        .toMutableStateFlow(viewModelScope)

    val labelSelector = _labelSelector.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()


    private var searchJob: Job? = null

    init {
        searchLabels("")
    }

    fun onSelect(event: SelectLabelsEvents) {
        when (event) {
            is SelectLabelsEvents.OnSelect -> {
                _labelSelector.update { selectedLabels ->
                    selectedLabels.map { label ->
                        if (label == event.state)
                            label.copy(isSelected = !label.isSelected).also { state ->
                                if (state.isSelected) _selectedRef.add(state)
                                else _selectedRef.remove(state)
                            }
                        else label
                    }
                }
            }
        }
    }

    fun searchLabels(search: String) {
        _query.update { search }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            labelRepo.searchLabels(search)
                .cancellable()
                .onEach { models -> _queriedLabels.update { models } }
                .launchIn(this)
        }
    }

    fun createLabel() {
        if (query.value.isNotEmpty())
            viewModelScope.launch {
                when (val res = labelRepo.createLabel(_query.value)) {
                    is Resource.Error -> _uiEvents
                        .emit(UIEvents.ShowSnackBar(res.message))

                    is Resource.Loading -> {}
                    is Resource.Success -> _uiEvents
                        .emit(UIEvents.ShowSnackBar("Added new label ${_query.value}"))
                }
            }
    }

}