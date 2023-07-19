package com.eva.reminders.presentation.feature_create

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eva.reminders.domain.repository.TaskRepository
import com.eva.reminders.domain.usecase.TaskValidator
import com.eva.reminders.presentation.feature_create.utils.AddTaskEvents
import com.eva.reminders.presentation.feature_create.utils.AddTaskState
import com.eva.reminders.presentation.feature_create.utils.SelectLabelState
import com.eva.reminders.presentation.feature_create.utils.TaskRemindersEvents
import com.eva.reminders.presentation.feature_create.utils.toCreateModel
import com.eva.reminders.presentation.feature_create.utils.toUpdateModel
import com.eva.reminders.presentation.feature_create.utils.toUpdateState
import com.eva.reminders.presentation.utils.NavConstants
import com.eva.reminders.presentation.utils.ShowContent
import com.eva.reminders.presentation.utils.UIEvents
import com.eva.reminders.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val presenter: AddLabelToTasksPresenter,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val validator = TaskValidator()

    private val _uiEvents = MutableSharedFlow<UIEvents>()
    val uiEvents = _uiEvents.asSharedFlow()

    val labelQuery = presenter.labelQuery

    val selectedLabels = presenter.stateToModels.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        emptyList()
    )

    val labelsSelectorStates = presenter.selectedLabelFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        emptyList()
    )

    // In case the task is loaded from the database show the spinner for some time
    private val _taskState = MutableStateFlow(AddTaskState())
    private val _isLoading = MutableStateFlow(false)

    val taskState = combine(_taskState, _isLoading) { tasks, loading ->
        ShowContent(isLoading = loading, content = tasks)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(500L),
        ShowContent(isLoading = true, content = AddTaskState())
    )

    private var searchLabelJob: Job? = null

    init {
        setCurrentData()
        searchLabels("")
    }


    fun onSelect(state: SelectLabelState) = presenter.onLabelSelect(state)
    fun createNewLabel() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val res = presenter.createLabels()) {
                is Resource.Error -> _uiEvents
                    .emit(UIEvents.ShowSnackBar(res.message))

                is Resource.Success -> _uiEvents
                    .emit(UIEvents.ShowSnackBar("Added new label ${res.data.label}"))

                else -> {}
            }
        }
    }

    fun searchLabels(search: String = "") = viewModelScope
        .launch(Dispatchers.IO) {
            searchLabelJob?.cancel()
            searchLabelJob = presenter
                .onSearch(search)
                .launchIn(this)
        }


    private fun setCurrentData() {
        val taskId = savedStateHandle.get<Int>(NavConstants.TASK_ID)
        if (taskId != null && taskId != -1) {

            viewModelScope.launch(Dispatchers.IO) {
                when (val resource = repository.getTaskById(taskId)) {
                    is Resource.Error -> {
                        _uiEvents.emit(UIEvents.ShowSnackBar(resource.message))
                        Log.d("TASK", resource.message)
                    }

                    is Resource.Loading -> _isLoading.update { true }
                    is Resource.Success -> {
                        val state = resource.data.toUpdateState()
                        _taskState.update { state.copy(isCreate = false) }
                        presenter.setSelectedLabels(resource.data.labels)

                    }
                }
            }
        } else {
            _isLoading.update { false }
            _taskState.update { it.copy(isCreate = true) }
            presenter.clearAllLabels()
        }
    }

    private fun onReminderEvents(event: TaskRemindersEvents) {
        when (event) {
            is TaskRemindersEvents.OnDateChanged -> _taskState.update {
                it.copy(reminderState = it.reminderState.copy(date = event.date))
            }

            is TaskRemindersEvents.OnReminderChanged -> _taskState.update {
                it.copy(reminderState = it.reminderState.copy(frequency = event.frequency))
            }

            is TaskRemindersEvents.OnTimeChanged -> _taskState.update {
                it.copy(reminderState = it.reminderState.copy(time = event.time))
            }

            is TaskRemindersEvents.OnIsExactChange -> _taskState.update {
                it.copy(reminderState = it.reminderState.copy(isExact = event.isExact))
            }
        }
    }

    fun onAddTaskEvents(event: AddTaskEvents) {
        when (event) {
            is AddTaskEvents.OnColorChanged -> _taskState.update { it.copy(color = event.taskColor) }
            is AddTaskEvents.OnContentChange -> _taskState.update { it.copy(content = event.content) }
            AddTaskEvents.OnSubmit -> onSubmit()
            is AddTaskEvents.OnTitleChange -> _taskState.update { it.copy(title = event.title) }
            AddTaskEvents.ReminderStatePicked -> _taskState.update { it.copy(isReminderPresent = true) }
            AddTaskEvents.ReminderStateUnpicked -> _taskState.update { it.copy(isReminderPresent = false) }
            AddTaskEvents.ToggleArchive -> _taskState.update { it.copy(isArchived = !it.isArchived) }
            AddTaskEvents.TogglePinned -> _taskState.update { it.copy(isPinned = !it.isPinned) }
            is AddTaskEvents.OnReminderEvent -> onReminderEvents(event.event)
            AddTaskEvents.OnDelete -> onDelete()
            AddTaskEvents.MakeCopy -> makeCopy()
        }
    }

    private fun onSubmit() {
        val isUpdate = !_taskState.value.isCreate
        val labels = selectedLabels.value
        if (isUpdate) {

            val updateModel = _taskState.value.toUpdateModel(labels = labels)
            val validate = validator.updateValidator(updateModel)

            viewModelScope.launch(Dispatchers.IO) {
                if (validate.isValid) {
                    when (val change = repository.updateTask(updateModel)) {
                        is Resource.Error -> _uiEvents.emit(UIEvents.ShowSnackBar(change.message))
                        is Resource.Loading -> {}
                        is Resource.Success -> _uiEvents.emit(UIEvents.NavigateBack)
                    }
                } else {
                    _uiEvents.emit(
                        UIEvents.ShowSnackBar(validate.error ?: "Cannot update the task")
                    )
                }
            }
        } else {
            val createModel = _taskState.value.toCreateModel(labels = labels)
            val validate = validator.createValidator(createModel)

            viewModelScope.launch(Dispatchers.IO) {
                if (validate.isValid) {
                    when (val change = repository.createTask(createModel)) {

                        is Resource.Error -> _uiEvents.emit(UIEvents.ShowSnackBar(change.message))
                        is Resource.Loading -> {}
                        is Resource.Success -> _uiEvents.emit(UIEvents.NavigateBack)

                    }
                } else {
                    _uiEvents.emit(
                        UIEvents.ShowSnackBar(validate.error ?: "Cannot create the task")
                    )
                }
            }
        }
    }

    private fun onDelete() {

        val updateModel = _taskState.value.toUpdateModel(labels = selectedLabels.value)

        viewModelScope.launch(Dispatchers.IO) {
            when (val resource = repository.deleteTask(updateModel)) {
                is Resource.Error -> _uiEvents.emit(UIEvents.ShowSnackBar(resource.message))
                is Resource.Loading -> {}
                is Resource.Success -> _uiEvents.emit(UIEvents.NavigateBack)
            }
        }
    }

    private fun makeCopy() {
        val createModel = _taskState.value.toCreateModel(labels = selectedLabels.value)
        val validate = validator.createValidator(createModel)

        viewModelScope.launch(Dispatchers.IO) {
            if (validate.isValid) {

                when (val resource = repository.createTask(createModel)) {
                    is Resource.Error -> _uiEvents.emit(UIEvents.ShowSnackBar(resource.message))
                    is Resource.Loading -> {}
                    is Resource.Success -> _uiEvents.emit(UIEvents.NavigateBack)
                }

            } else {
                _uiEvents.emit(
                    UIEvents.ShowSnackBar(validate.error ?: "Cannot create the task")
                )
            }
        }
    }
}