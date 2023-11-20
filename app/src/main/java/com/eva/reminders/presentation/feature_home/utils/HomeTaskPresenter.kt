package com.eva.reminders.presentation.feature_home.utils

import com.eva.reminders.domain.enums.TaskColorEnum
import com.eva.reminders.domain.models.TaskModel
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.presentation.utils.HomeTabs
import com.eva.reminders.presentation.utils.ShowContent
import com.eva.reminders.utils.Resource

class HomeTaskPresenter(
    private val repository: TaskLabelsRepository
) {

    fun showTaskByTabs(
        tabs: HomeTabs,
        tasks: ShowContent<List<TaskModel>>
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
        searchType: SearchType,
        tabs: HomeTabs,
        tasks: List<TaskModel>
    ): SearchResultsType = when (searchType) {
        is SearchType.BasicSearch -> {

            val colorFilterResults = tasks.filter {
                Regex(".*${searchType.query}").matches(it.color.name)

//                val labelFilter =
//                    it.labels.map { label -> label.label.trim().lowercase() }
//                        .map { label -> Regex(".*${type.query}").matches(label) }
//                        .any()
//                colorFilter || labelFilter
            }

            val filterLabels =
                (repository.searchLabels(searchType.query) as? Resource.Success)?.data
                    ?: emptyList()


            val labelFilterResults = tasks
                .filter { task ->
                    filterLabels.all { label -> task.labels.contains(label) }
                }
            //The archived data should be not directly
            //But if the achieve tab is selected we can view all
            val results = when (tabs) {
                HomeTabs.Archived -> colorFilterResults + labelFilterResults
                else -> (colorFilterResults + labelFilterResults).filter { !it.isArchived }
            }

            SearchResultsType.SearchResults(results)
        }

        SearchType.BlankSearch -> SearchResultsType.NoResultsType
        is SearchType.ColorSearch -> when (tabs) {
            HomeTabs.Archived -> SearchResultsType
                .SearchResults(tasks.filter { it.color == searchType.color })

            else -> SearchResultsType
                .SearchResults(tasks.filter { it.color == searchType.color && !it.isArchived })
        }

        is SearchType.LabelSearch -> when (tabs) {
            HomeTabs.Archived -> SearchResultsType
                .SearchResults(tasks.filter { it.labels.contains(searchType.label) })

            else -> SearchResultsType
                .SearchResults(tasks.filter { it.labels.contains(searchType.label) && !it.isArchived })
        }
    }


    fun colorOptions(tasks: List<TaskModel>): List<TaskColorEnum> =
        tasks.map { model -> model.color }.distinct()

}