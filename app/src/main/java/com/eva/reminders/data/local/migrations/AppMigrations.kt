package com.eva.reminders.data.local.migrations

import androidx.room.RenameTable
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AppMigrations {

    // MANUAL MIGRATION FROM 2 TO 3
    val MIGRATE_ADD_FOREIGN_KEY_LABEL_TASK_REL = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Creates A New Schema with foreign keys added
            db.execSQL(
                """CREATE TABLE IF NOT EXISTS `TASK_LABEL_REL_COPY`(
                    `TASK_ID` INTEGER NOT NULL,
                    `LABEL_ID` INTEGER NOT NULL,
                     PRIMARY KEY(`TASK_ID`, `LABEL_ID`),
                     FOREIGN KEY(`TASK_ID`) REFERENCES `TaskEntity`(`TASK_ID`) ON UPDATE CASCADE ON DELETE CASCADE ,
                     FOREIGN KEY(`LABEL_ID`) REFERENCES `LabelEntity`(`LABEL_ID`) ON UPDATE CASCADE ON DELETE CASCADE 
                );
                     """.trimIndent()
            )
            // Copy the data from taskLabelRel to its copy
            db.execSQL(
                """
                INSERT INTO `TASK_LABEL_REL_COPY` (`TASK_ID`,`LABEL_ID`) 
                SELECT `TASK_ID`,`LABEL_ID` FROM TASK_LABEL_REL ;
            """.trimIndent()
            )
            // Drop the task_label_rel actual
            db.execSQL("DROP TABLE TASK_LABEL_REL;")
            // Rename it to its actual copy
            db.execSQL("ALTER TABLE TASK_LABEL_REL_COPY RENAME TO TASK_LABEL_REL;")
        }
    }

    // AUTO MIGRATION 3 TO 4
    @RenameTable(fromTableName = "TaskEntity", toTableName = "TASK_ENTITY")
    class RenameTaskEntityTable : AutoMigrationSpec

    // AUTO MIGRATION 4 TO 5
    @RenameTable(fromTableName = "LabelEntity", toTableName = "LABEL_ENTITY")
    class RenameLabelEntityTable : AutoMigrationSpec
}