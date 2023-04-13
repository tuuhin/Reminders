package com.eva.reminders.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.eva.reminders.data.local.entity.LabelEntity
import com.eva.reminders.data.local.entity.TaskEntity
import com.eva.reminders.data.local.entity.TaskLabelRel

data class TaskWithLabelRelation(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "TASK_ID",
        entityColumn = "LABEL_ID",
        associateBy = Junction(TaskLabelRel::class)
    )
    val labels: List<LabelEntity>
)
