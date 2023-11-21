package com.eva.reminders.di

import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.presentation.feature_create.AddLabelToTasksPresenter
import com.eva.reminders.presentation.feature_home.utils.HomeTaskPresenter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object PresenterModule {

    @Provides
    @ViewModelScoped
    fun homePresenter(repository: TaskLabelsRepository): HomeTaskPresenter =
        HomeTaskPresenter(repository)

    @Provides
    @ViewModelScoped
    fun addTaskPresenter(repository: TaskLabelsRepository): AddLabelToTasksPresenter =
        AddLabelToTasksPresenter(repository)
}