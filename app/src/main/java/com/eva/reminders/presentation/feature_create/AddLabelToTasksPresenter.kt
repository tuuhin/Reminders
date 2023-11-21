package com.eva.reminders.presentation.feature_create

import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.presentation.feature_create.utils.SelectLabelState
import com.eva.reminders.presentation.feature_create.utils.toSelectLabelStates
import com.eva.reminders.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class AddLabelToTasksPresenter(
    private val labelsRepository: TaskLabelsRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val _queriedLabels = MutableStateFlow(emptyList<SelectLabelState>())

    // we only want the id to check if it's been added so a list of TaskLabelModel will do the trick
    private val _selectedLabels = MutableStateFlow(emptyList<TaskLabelModel>())


    val searchedLabels = combine(_queriedLabels, _selectedLabels) { queried, selected ->
        queried.map { state ->
            val isSelected = selected.any { it.id == state.model.id }
            // if its selected its already in selected label list so lets
            // only update the isSelected value otherwise create a new one
            state.copy(isSelected = isSelected)
        }
    }

    val selectedLabelsAsModels = searchedLabels.map { states ->
        states.filter { it.isSelected }.map { it.model }
    }

    fun setSelectedLabels(labels: List<TaskLabelModel>) = _selectedLabels.update { labels }

    suspend fun onSearch(search: String): Resource<List<TaskLabelModel>> {
        val results = withContext(dispatcher) {
            labelsRepository.searchLabels(search)
        }
        // if the result is a successful one take that otherwise not
        val resultsAsSuccess = results as? Resource.Success
        resultsAsSuccess?.data?.let { models ->
            _queriedLabels.update { models.toSelectLabelStates() }
        }
        return results
    }


    // Don't seem necessary but during a creation route all labels are cleared initially
    fun clearAllLabels() = _queriedLabels.update { emptyList() }

    fun onLabelSelect(state: SelectLabelState) {
        if (state.isSelected) {
            _selectedLabels.update { states -> states.filter { it.id != state.model.id } }
            return
        }
        _selectedLabels.update { states -> states + state.model }
    }

    suspend fun createLabels(label: String): Resource<TaskLabelModel> = withContext(dispatcher) {
        if (label.isNotEmpty()) {
            val trimmedLabel = label.trim()
            labelsRepository.createLabel(trimmedLabel)
        }
        Resource.Error(message = "Label cannot be empty")
    }
}