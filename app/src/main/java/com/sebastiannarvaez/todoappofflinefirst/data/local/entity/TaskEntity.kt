package com.sebastiannarvaez.todoappofflinefirst.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sebastiannarvaez.todoappofflinefirst.domain.models.TaskModel
import com.sebastiannarvaez.todoappofflinefirst.presentation.viewmodel.TaskCategory

@Entity(
    tableName = "tasks",
    indices = [
        Index(value = ["title"], unique = true)
    ]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0L,
    val remoteId: String? = null,
    val title: String,
    val description: String,
    val category: TaskCategory,
    val isDone: Boolean = false,

    @ColumnInfo(defaultValue = "0")
    val isSynced: Boolean,
    @ColumnInfo(defaultValue = "0")
    val isDeleted: Boolean,
    val lastModified: Long = System.currentTimeMillis()
) {
    fun toDomain(): TaskModel {
        return TaskModel(
            localId = localId,
            remoteId = remoteId,
            title = title,
            description = description,
            category = category,
            isDone = isDone,
            isSynced = isSynced,
            isDeleted = isDeleted
        )
    }
}
