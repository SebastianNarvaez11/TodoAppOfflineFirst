package com.sebastiannarvaez.todoappofflinefirst.domain.models

import com.sebastiannarvaez.todoappofflinefirst.data.local.entity.TaskEntity
import com.sebastiannarvaez.todoappofflinefirst.data.remote.request.TaskCreateRequest
import com.sebastiannarvaez.todoappofflinefirst.data.remote.request.TaskUpdateRequest
import com.sebastiannarvaez.todoappofflinefirst.presentation.viewmodel.TaskCategory

data class TaskModel(
    val id: String,
    val title: String,
    val description: String,
    val category: TaskCategory,
    val isDone: Boolean = false
)

data class TaskUpdateParams(
    val title: String? = null,
    val description: String? = null,
    val category: TaskCategory? = null,
    val isDone: Boolean? = null,
)

fun TaskModel.toEntity(): TaskEntity {
    return TaskEntity(
        id = id.toLong(),
        title = title,
        description = description,
        category = category,
        isDone = isDone
    )
}

fun TaskModel.toTaskCreateRequest(): TaskCreateRequest {
    return TaskCreateRequest(
        title = title,
        description = description,
        category = category.name,
        isCheck = isDone
    )
}

fun TaskUpdateParams.toTaskUpdateRequest(): TaskUpdateRequest {
    return TaskUpdateRequest(
        title = title,
        description = description,
        category = category?.name,
        isCheck = isDone
    )
}