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
import com.eva.reminders.presentation.feature_labels.utils.LabelSortOrder
import com.eva.reminders.presentation.feature_labels.utils.toEditStates
import com.eva.reminders.presentation.feature_labels.utils.toUpdateModel
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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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

    private val _sortOrder = MutableStateFlow(LabelSortOrder.REGULAR)
    val labelsSortOrder = _sortOrder.asStateFlow()

    private val _editLabelEvents = MutableStateFlow<EditLabelEvents>(EditLabelEvents.ShowAllLabels)

    private val _editableLabelStatesFlow = combine(_labels, _sortOrder) { models, order ->
        val labelEditStates = models.toEditStates()
        when (order) {
            LabelSortOrder.REGULAR -> labelEditStates
            LabelSortOrder.ALPHABETICALLY_ASC -> labelEditStates.sortedBy { it.previousLabel }
            LabelSortOrder.ALPHABETICALLY_DESC -> labelEditStates.sortedByDescending { it.previousLabel }
        }
    }

    val editLabelStates = combine(_editableLabelStatesFlow, _editLabelEvents) { labels, event ->
        when (event) {
            is EditLabelEvents.OnLabelValueUpdate -> labels.map { label ->
                if (label.labelId == event.labelId)
                    label.copy(updatedLabel = event.text, isEdit = true)
                else label
            }

            is EditLabelEvents.ToggleEnabled -> labels.map { label ->
                if (label.labelId == event.labelId)
                    label.copy(isEdit = !label.isEdit, updatedLabel = "")
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

    fun onSortOderChange(order: LabelSortOrder) = _sortOrder.update { order }

    fun onCreateLabelEvent(event: CreateLabelEvents) {
        when (event) {
            is CreateLabelEvents.OnValueChange -> _newLabelState
                .update { state ->
                    state.isError?.let {
                        // if there is error string set isError to null
                        state.copy(label = event.text, isError = null)
                    } ?: state.copy(label = event.text)
                }

            CreateLabelEvents.OnSubmit -> createLabel()
        }
    }

    fun onLabelAction(actions: EditLabelsActions) {
        //  _editLabelEvents.update { EditLabelEvents.ShowAllLabels }
        when (actions) {
            is EditLabelsActions.OnDelete -> actions.item.model
                ?.let { model -> onDelete(model) }

            is EditLabelsActions.OnUpdate -> actions.item.toUpdateModel()
                ?.let { model -> onUpdate(model) }
        }
    }

    fun onUpdateLabelEvent(event: EditLabelEvents) = _editLabelEvents.update { event }

    private fun getSavedLabels() = viewModelScope.launch {
        labelRepo.getLabels()
            .onEach { res ->
                when (res) {
                    is Resource.Error -> _uiEvents.emit(UIEvents.ShowSnackBar(res.message))
                    is Resource.Success -> _labels.update { res.data }
                    else -> {}
                }
            }
            .launchIn(this)
    }

    private fun onDelete(label: TaskLabelModel) = viewModelScope.launch(Dispatchers.IO) {
        when (val res = labelRepo.deleteLabel(label)) {
            is Resource.Error -> _uiEvents
                .emit(UIEvents.ShowSnackBar(message = res.message))

            is Resource.Success -> _uiEvents
                .emit(UIEvents.ShowSnackBar(message = "${label.label} is removed"))

            else -> {}
        }
    }


    private fun onUpdate(label: TaskLabelModel) {
        val validator = labelValidator.validate(
            label = label.label,
            others = _labels.value.map { it.label }
        )

        viewModelScope.launch {
            if (validator.isValid) {
                //just trimming off the extra blank spaces
                val trimmedLabel = label.copy(label = label.label.trim())

                when (val res = labelRepo.updateLabel(trimmedLabel)) {
                    is Resource.Error -> _uiEvents.emit(UIEvents.ShowSnackBar(message = res.message))

                    else -> {}
                }
            } else _uiEvents.emit(
                UIEvents.ShowSnackBar(validator.error ?: "Cannot update label")
            )
        }
    }

    private fun createLabel() {
        val labelText = _newLabelState.value.label.trim()

        val validator = labelValidator.validate(
            label = labelText,
            others = _labels.value.map { it.label }
        )

        if (validator.isValid) {
            viewModelScope.launch {

                when (val res = labelRepo.createLabel(labelText)) {
                    is Resource.Error -> _uiEvents.emit(UIEvents.ShowSnackBar(message = res.message))

                    is Resource.Success -> _newLabelState.update { CreateLabelState() }
                    else -> {}
                }
            }
        } else _newLabelState.update { state -> state.copy(isError = validator.error) }
    }
}