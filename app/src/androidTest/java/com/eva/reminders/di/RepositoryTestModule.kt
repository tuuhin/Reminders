package com.eva.reminders.di

import com.eva.reminders.data.local.dao.LabelsDao
import com.eva.reminders.data.local.dao.TaskDao
import com.eva.reminders.data.local.dao.TaskLabelRelDao
import com.eva.reminders.data.repository.TaskLabelsRepoTestImpl
import com.eva.reminders.data.repository.TaskRepoInstTestImpl
import com.eva.reminders.domain.repository.TaskLabelsRepository
import com.eva.reminders.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
object RepositoryTestModule {
    @Provides
    fun providesTestTaskLabelRepoImpl(
        labelsDao: LabelsDao,
    ): TaskLabelsRepository = TaskLabelsRepoTestImpl(labelDao = labelsDao)

    @Provides
    fun providesTestTaskRepo(
        taskDao: TaskDao,
        taskLabelRelDao: TaskLabelRelDao,
    ): TaskRepository = TaskRepoInstTestImpl(
        taskDao = taskDao,
        taskLabelRel = taskLabelRelDao
    )
}