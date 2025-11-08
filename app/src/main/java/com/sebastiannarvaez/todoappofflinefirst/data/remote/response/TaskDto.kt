package com.sebastiannarvaez.todoappofflinefirst.data.remote.response

import com.sebastiannarvaez.todoappofflinefirst.domain.models.TaskModel
import com.sebastiannarvaez.todoappofflinefirst.presentation.viewmodel.TaskCategory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskDto(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    @SerialName("is_check") val isChecked: Boolean,
    @SerialName("created_at") val createdAt: String
)

fun TaskDto.toDomain(): TaskModel {
    return TaskModel(
        id = this.id,
        title = this.title,
        description = this.description,
        category = TaskCategory.valueOf(this.category),
        isDone = this.isChecked
    )
}