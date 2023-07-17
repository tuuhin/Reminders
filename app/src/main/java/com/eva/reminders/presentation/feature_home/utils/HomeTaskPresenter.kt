package com.eva.reminders.presentation.feature_home.utils

import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.presentation.utils.HomeTabs
import com.eva.reminders.presentation.utils.ShowContent
import kotlinx.coroutines.flow.first

class HomeTaskPresenter(
    private val repository: TaskLabelsRepository
) {

    fun showTaskByTabs(
        tabs: HomeTabs, tasks: ShowContent<List<TaskModel>>
    ): ShowContent<List<TaskModel>> {
        return when (tabs) {
            HomeTabs.AllReminders -> tasks.copy(content = tasks.content
                .filter { !it.isArchived })

            HomeTabs.Archived -> tasks.copy(content = tasks.content
                .filter { it.isArchived })

            HomeTabs.NonScheduled -> tasks.copy(content = tasks.content
                .filter { !it.isArchived && it.reminderAt.at == null })

            HomeTabs.Scheduled -> tasks.copy(content = tasks.content
                .filter { !it.isArchived && it.reminderAt.at != null })
        }
    }

    suspend fun searchResultsType(
        type: SearchType,
        tasks: List<TaskModel>
    ): SearchResultsType = when (type) {
        is SearchType.BasicSearch -> {

            val colorFilterResults = tasks.filter {
                Regex(".*${type.query}").matches(it.color.name)

//                val labelFilter =
//                    it.labels.map { label -> label.label.trim().lowercase() }
//                        .map { label -> Regex(".*${type.query}").matches(label) }
//                        .any()
//                colorFilter || labelFilter
            }

            val filterLabels = repository.searchLabels(type.query).first()


            val labelFilterResults = tasks
                .filter { task ->
                    filterLabels.all { label -> task.labels.contains(label) }
                }

            SearchResultsType.SearchResults(colorFilterResults + labelFilterResults)
        }

        SearchType.BlankSearch -> SearchResultsType.NoResultsType
        is SearchType.ColorSearch -> SearchResultsType.SearchResults(
            tasks.filter { it.color == type.color }
        )

        is SearchType.LabelSearch -> SearchResultsType.SearchResults(
            tasks.filter { it.labels.contains(type.label) }
        )
    }


    fun colorOptions(tasks: List<TaskModel>): List<TaskColorEnum> =
        tasks.map { model -> model.color }.distinct()

}