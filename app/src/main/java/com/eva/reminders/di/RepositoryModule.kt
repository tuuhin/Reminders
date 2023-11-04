package com.eva.reminders.di

import com.eva.reminders.data.local.dao.LabelsDao
import com.eva.reminders.data.local.dao.LabelsFtsDao
import com.eva.reminders.data.local.dao.TaskDao
import com.eva.reminders.data.local.dao.TaskLabelRelDao
import com.eva.reminders.data.repository.TaskLabelsRepoImpl
import com.eva.reminders.data.repository.TaskRepoImpl
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.domain.repository.TaskRepository
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
    fun getTaskLabelDao(
        labelsDao: LabelsDao,
        labelsFtsDao: LabelsFtsDao
    ): TaskLabelsRepository =
        TaskLabelsRepoImpl(
            labelDao = labelsDao,
            labelFts = labelsFtsDao
        )

    @Provides
    @ViewModelScoped
    fun getTaskDao(
        taskDao: TaskDao,
        taskLabelRelDao: TaskLabelRelDao,
        alarmRepo: AlarmManagerRepo
    ): TaskRepository =
        TaskRepoImpl(
            taskDao = taskDao,
            taskLabelRel = taskLabelRelDao,
            alarmRepo = alarmRepo
        )

}