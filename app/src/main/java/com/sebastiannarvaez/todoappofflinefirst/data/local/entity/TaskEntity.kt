package com.sebastiannarvaez.todoappofflinefirst.data.local.entity

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
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    val description: String,
    val category: TaskCategory,
    val isDone: Boolean = false
) {
    fun toDomain(): TaskModel {
        return TaskModel(
            id = id.toString(),
            title = title,
            description = description,
            category = category,
            isDone = isDone
        )
    }
}
