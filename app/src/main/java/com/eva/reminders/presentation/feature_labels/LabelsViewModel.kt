package com.eva.reminders.presentation.feature_labels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.presentation.feature_labels.utils.CreateLabelEvents
import com.eva.reminders.presentation.feature_labels.utils.CreateLabelState
import com.eva.reminders.presentation.feature_labels.utils.EditLabelEvents
import com.eva.reminders.presentation.feature_labels.utils.EditLabelState
import com.eva.reminders.presentation.utils.UIEvents
import com.eva.reminders.presentation.utils.toMutableStateFlow
import com.eva.reminders.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabelsViewModel @Inject constructor(
    private val labelRepo: TaskLabelsRepository
) : ViewModel() {

    private val _labels = MutableStateFlow<List<TaskLabelModel>>(emptyList())
    val allLabels = _labels.asStateFlow()

    private val _createLabelState = MutableStateFlow(CreateLabelState())
    val createLabelState = _createLabelState.asStateFlow()

    private val _updateLabels = _labels.map { models ->
        models.map { model -> EditLabelState.fromLabel(model) }
    }.stateIn(viewModelScope, SharingStarted.Lazily, initialValue = emptyList())
        .toMutableStateFlow(viewModelScope)

    val updatedLabels = _updateLabels.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UIEvents>()
    val uiEvents = _uiEvents.asSharedFlow()

    init {
        viewModelScope.launch {
            labelRepo.getLabels()
                .onEach { models ->
                    _labels.update { models }
                }
                .launchIn(this)
        }
    }

    fun onCreateLabelEvent(event: CreateLabelEvents) {
        when (event) {
            is CreateLabelEvents.OnValueChange -> _createLabelState.update {
                it.copy(label = event.text)
            }
            CreateLabelEvents.ToggleEnabled -> _createLabelState.update {
                it.copy(isEnabled = !it.isEnabled)
            }
            CreateLabelEvents.OnSubmit -> createLabel()
        }
    }

    fun onUpdateLabelEvent(event: EditLabelEvents) {
        when (event) {
            is EditLabelEvents.OnDelete -> event.item.model?.let { onDelete(it) }
            is EditLabelEvents.OnUpdate -> event.item.toModel()?.let { onUpdate(it) }
            is EditLabelEvents.OnValueChange -> {
                _updateLabels.update { states ->
                    states.map { state ->
                        if (state == event.item)
                            state.copy(text = event.text)
                        else state
                    }
                }
            }
            is EditLabelEvents.ToggleEnabled -> {
                _updateLabels.update { states ->
                    states.map { state ->
                        if (state == event.item)
                            state.copy(isEdit = !state.isEdit)
                        else state
                    }

                }
            }
        }
    }

    private fun onDelete(label: TaskLabelModel) {
        viewModelScope.launch {
            when (val res = labelRepo.deleteLabel(label)) {
                is Resource.Error -> _uiEvents.emit(UIEvents.ShowSnackBar(message = res.message))
                else -> {}
            }
        }
    }

    private fun onUpdate(label: TaskLabelModel) {
        viewModelScope.launch {
            when (val res = labelRepo.updateLabel(label)) {
                is Resource.Error -> _uiEvents.emit(UIEvents.ShowSnackBar(message = res.message))
                else -> {}
            }
        }
    }

    private fun createLabel() {
        viewModelScope.launch {
            when (val res = labelRepo.createLabel(_createLabelState.value.label)) {
                is Resource.Error -> _uiEvents.emit(UIEvents.ShowSnackBar(message = res.message))
                is Resource.Loading -> {}
                is Resource.Success -> _createLabelState.update { CreateLabelState() }
            }
        }
    }
}