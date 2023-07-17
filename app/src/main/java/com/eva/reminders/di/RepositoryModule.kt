package com.eva.reminders.di

import com.eva.reminders.data.local.AppDataBase
import com.eva.reminders.data.repository.TaskLabelsRepoImpl
import com.eva.reminders.data.repository.TaskRepoImpl
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.domain.repository.TaskRepository
import com.eva.reminders.presentation.feature_home.utils.HomeTaskPresenter
import com.eva.reminders.services.AlarmManagerRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun getTaskLabelDao(database: AppDataBase): TaskLabelsRepository =
        TaskLabelsRepoImpl(
            labelDao = database.taskLabelDao,
            labelFts = database.labelsFts
        )

    @Provides
    @ViewModelScoped
    fun getTaskDao(database: AppDataBase, alarmRepo: AlarmManagerRepo): TaskRepository =
        TaskRepoImpl(
            taskDao = database.taskDao,
            taskLabelRel = database.taskLabelRelDao,
            alarmRepo = alarmRepo
        )

    @Provides
    fun homePresenter(repository: TaskLabelsRepository): HomeTaskPresenter =
        HomeTaskPresenter(repository)
}