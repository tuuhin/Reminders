package com.eva.reminders.data.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.eva.reminders.data.local.entity.LabelEntity
import com.eva.reminders.data.local.entity.TaskEntity
import com.eva.reminders.data.local.entity.TaskLabelRel

data class LabelWithTaskRelation(
    @Embedded val label: LabelEntity,
    @Relation(
        parentColumn = "LABEL_ID",
        entityColumn = "TASK_ID",
        associateBy = Junction(TaskLabelRel::class)
    )
    val tasks: List<TaskEntity>
)
