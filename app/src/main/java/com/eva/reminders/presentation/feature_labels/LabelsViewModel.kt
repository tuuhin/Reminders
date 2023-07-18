package com.eva.reminders.presentation.feature_labels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.domain.usecase.LabelValidator
import com.eva.reminders.presentation.feature_labels.utils.CreateLabelEvents
import com.eva.reminders.presentation.feature_labels.utils.CreateLabelState
import com.eva.reminders.presentation.feature_labels.utils.EditLabelEvents
import com.eva.reminders.presentation.feature_labels.utils.EditLabelsActions
import com.eva.reminders.presentation.feature_labels.utils.toEditState
import com.eva.reminders.presentation.feature_labels.utils.toUpdateModel
import com.eva.reminders.presentation.utils.UIEvents
import com.eva.reminders.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabelsViewModel @Inject constructor(
    private val labelRepo: TaskLabelsRepository,
) : ViewModel() {
    private val labelValidator: LabelValidator = LabelValidator()

    private val _labels = MutableStateFlow<List<TaskLabelModel>>(emptyList())
    val allLabels = _labels.asStateFlow()

    private val _newLabelState = MutableStateFlow(CreateLabelState())
    val newLabelState = _newLabelState.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UIEvents>()
    val uiEvents = _uiEvents.asSharedFlow()

    private val _editLabelEvents = MutableStateFlow<EditLabelEvents>(EditLabelEvents.ShowAllLabels)

    private val _updatedLabelModelFlow = _labels.map { models ->
        models.map { model -> model.toEditState() }
    }


    val editLabelStates = combine(_updatedLabelModelFlow, _editLabelEvents) { labels, event ->
        when (event) {
            is EditLabelEvents.OnValueChange -> labels.map { label ->
                if (label.labelId == event.item.labelId)
                    label.copy(updatedLabel = event.text)
                else label
            }

            is EditLabelEvents.ToggleEnabled -> labels.map { label ->
                if (label.labelId == event.item.labelId)
                    label.copy(isEdit = !event.item.isEdit)
                else label
            }

            else -> labels
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        initialValue = emptyList()
    )


    init {
        getSavedLabels()
    }

    private fun getSavedLabels() {
        viewModelScope.launch(Dispatchers.IO) {
            labelRepo.getLabels()
                .catch { err -> err.printStackTrace() }
                .onEach { models -> _labels.update { models } }
                .launchIn(this)
        }
    }

    fun onCreateLabelEvent(event: CreateLabelEvents) {
        when (event) {
            is CreateLabelEvents.OnValueChange -> _newLabelState
                .update { state -> state.copy(label = event.text) }

            CreateLabelEvents.ToggleEnabled -> _newLabelState
                .update { state -> CreateLabelState(isEnabled = !state.isEnabled) }

            CreateLabelEvents.OnSubmit -> createLabel()
        }
    }

    fun onLabelAction(actions: EditLabelsActions) {
        _editLabelEvents.update { EditLabelEvents.ShowAllLabels }
        when (actions) {
            is EditLabelsActions.OnDelete -> actions.item.model
                ?.let { model -> onDelete(model) }

            is EditLabelsActions.OnUpdate -> actions.item.toUpdateModel()
                ?.let { model -> onUpdate(model) }
        }
    }

    fun onUpdateLabelEvent(event: EditLabelEvents) = _editLabelEvents.update { event }

    private fun onDelete(label: TaskLabelModel) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val res = labelRepo.deleteLabel(label)) {
                is Resource.Error -> _uiEvents
                    .emit(UIEvents.ShowSnackBar(message = res.message))

                is Resource.Success -> _uiEvents
                    .emit(UIEvents.ShowSnackBar(message = "${label.label} is removed"))

                else -> {}
            }
        }
    }

    private fun onUpdate(label: TaskLabelModel) {
        val validator = labelValidator.validate(
            label = label.label,
            others = _labels.value.map { it.label }
        )

        viewModelScope.launch(Dispatchers.IO) {
            if (validator.isValid)
                when (val res = labelRepo.updateLabel(label)) {
                    is Resource.Error -> _uiEvents
                        .emit(UIEvents.ShowSnackBar(message = res.message))

                    else -> {}
                }
            else _uiEvents.emit(UIEvents.ShowSnackBar(validator.error ?: "Cannot update label"))
        }
    }

    private fun createLabel() {
        val validator = labelValidator.validate(
            label = _newLabelState.value.label,
            others = _labels.value.map { it.label }
        )

        viewModelScope.launch(Dispatchers.IO) {
            if (validator.isValid) {
                val trimmedLabel = _newLabelState.value.label.trim()
                when (val res = labelRepo.createLabel(trimmedLabel)) {
                    is Resource.Error -> _uiEvents
                        .emit(UIEvents.ShowSnackBar(message = res.message))

                    is Resource.Loading -> {}
                    is Resource.Success -> _newLabelState.update { CreateLabelState() }
                }
            } else
                _newLabelState.update { state -> state.copy(isError = validator.error) }
        }
    }
}