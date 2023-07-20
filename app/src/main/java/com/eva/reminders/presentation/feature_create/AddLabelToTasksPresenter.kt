package com.eva.reminders.presentation.feature_create

import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.presentation.feature_create.utils.SelectLabelState
import com.eva.reminders.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class AddLabelToTasksPresenter(
    private val labelsRepository: TaskLabelsRepository
) {

    private val _queriedLabels = MutableStateFlow(emptyList<TaskLabelModel>())

    private val _selectedLabelFlow = MutableStateFlow(emptyList<SelectLabelState>())

    val selectedLabelFlow = combine(_queriedLabels, _selectedLabelFlow) { query, selected ->
        query.map { model ->
            val isSelected = selected.any { state -> state.idx == model.id }
            SelectLabelState(idx = model.id, label = model.label, isSelected = isSelected)
        }
    }

    val stateToModels = _selectedLabelFlow
        .map { states -> states.map { state -> state.toModel() } }

    fun setSelectedLabels(labels: List<TaskLabelModel>) {
        _selectedLabelFlow.update { emptyList() }
        _selectedLabelFlow.update {
            labels.map { SelectLabelState(it.id, it.label, isSelected = true) }
        }
    }


    suspend fun onSearch(search: String = ""): Flow<List<TaskLabelModel>> {
        return labelsRepository
            .searchLabels(search)
            .catch { err -> err.printStackTrace() }
            .cancellable()
            .onEach { models ->
                _queriedLabels.update { models }
            }
    }

    // Don't seems necessary but during a create route all labels are cleared
    fun clearAllLabels() = _selectedLabelFlow.update { emptyList() }

    fun onLabelSelect(state: SelectLabelState) {
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

    suspend fun createLabels(label:String): Resource<TaskLabelModel> {
        if (label.isNotEmpty()) {
            val trimmedLabel = label.trim()
            return labelsRepository.createLabel(trimmedLabel)
        }
        return Resource.Error(message = "Label cannot be empty")
    }
}