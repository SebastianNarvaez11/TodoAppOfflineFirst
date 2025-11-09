package com.sebastiannarvaez.todoappofflinefirst.presentation.viewmodel

import com.sebastiannarvaez.todoappofflinefirst.domain.models.TaskModel

data class TasksUiState(
    val isLoading: Boolean = false,
    val isRefreshingFromRemote: Boolean = false,
    val isSavingTask: Boolean = false,
    val isUpdatingTask: Boolean = false,
    val error: String? = null,
    val errorSavingTask: String? = null,
    val tasks: List<TaskModel> = emptyList(),
    val filterSelectedCategory: TaskCategory? = null,
    val visibleAddTaskDialog: Boolean = false
)
