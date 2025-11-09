package com.sebastiannarvaez.todoappofflinefirst.data.remote.response

import com.sebastiannarvaez.todoappofflinefirst.domain.models.TaskModel
import com.sebastiannarvaez.todoappofflinefirst.presentation.viewmodel.TaskCategory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskDto(
    val remoteId: String,
    val localId: Long,
    val title: String,
    val description: String,
    val category: String,
    val isSynced: Boolean,
    val isDeleted: Boolean,
    @SerialName("is_check") val isChecked: Boolean,
    @SerialName("created_at") val createdAt: String
)

fun TaskDto.toDomain(): TaskModel {
    return TaskModel(
        localId = localId,
        remoteId = remoteId,
        title = title,
        description = description,
        category = TaskCategory.valueOf(this.category),
        isDone = this.isChecked,
        isSynced = isSynced,
        isDeleted = isDeleted
    )
}