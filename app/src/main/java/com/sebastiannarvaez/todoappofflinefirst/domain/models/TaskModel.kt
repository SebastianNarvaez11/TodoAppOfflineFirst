package com.sebastiannarvaez.todoappofflinefirst.domain.models

import com.sebastiannarvaez.todoappofflinefirst.data.local.entity.TaskEntity
import com.sebastiannarvaez.todoappofflinefirst.data.remote.request.TaskCreateRequest
import com.sebastiannarvaez.todoappofflinefirst.data.remote.request.TaskUpdateRequest
import com.sebastiannarvaez.todoappofflinefirst.presentation.viewmodel.TaskCategory

data class TaskModel(
    val localId: Long,
    val remoteId: String? = null,
    val title: String,
    val description: String,
    val category: TaskCategory,
    val isDone: Boolean = false,
    val isSynced: Boolean = false,
    val isDeleted: Boolean = false,
)

data class TaskUpdateParams(
    val title: String? = null,
    val description: String? = null,
    val category: TaskCategory? = null,
    val isDone: Boolean? = null,
    val isSynced: Boolean? = null,
    val isDeleted: Boolean? = null,
)

fun TaskModel.toEntity(): TaskEntity {
    return TaskEntity(
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

fun TaskModel.toTaskCreateRequest(): TaskCreateRequest {
    return TaskCreateRequest(
        localId = localId,
        remoteId = remoteId,
        title = title,
        description = description,
        category = category.name,
        isChecked = isDone,
        isSynced = isSynced,
        isDeleted = isDeleted,
    )
}

fun TaskUpdateParams.toTaskUpdateRequest(): TaskUpdateRequest {
    return TaskUpdateRequest(
        title = title,
        description = description,
        category = category?.name,
        isChecked = isDone
    )
}

fun TaskModel.toTaskUpdateRequest(): TaskUpdateRequest {
    return TaskUpdateRequest(
        title = title,
        description = description,
        category = category.name,
        isChecked = isDone,
        isSynced = isSynced,
        isDeleted = isDeleted,
    )
}