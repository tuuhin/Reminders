package com.eva.reminders.presentation.feature_home.utils

import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.TaskLabelModel
import com.eva.reminders.domain.models.TaskModel

sealed interface SearchType {
    object BlankSearch : SearchType
    data class LabelSearch(val labelModel: TaskLabelModel) : SearchType
    data class ColorSearch(val search: TaskColorEnum) : SearchType
    data class BasicSearch(val query: String) : SearchType

}

sealed interface SearchResultsType {
    object NoResultsType : SearchResultsType
    data class SearchResults(val tasks: List<TaskModel>) : SearchResultsType
}