package com.sebastiannarvaez.todoappofflinefirst.domain.repository

import com.sebastiannarvaez.todoappofflinefirst.domain.models.TaskModel
import com.sebastiannarvaez.todoappofflinefirst.domain.models.TaskUpdateParams
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTask(): Flow<List<TaskModel>>

    suspend fun getAllTaskApi(): Result<List<TaskModel>>

    suspend fun createTask(task: TaskModel): Result<Long>

    suspend fun createTaskApi(task: TaskModel): Result<TaskModel>

    suspend fun updateTask(task: TaskModel): Result<Int>

    suspend fun updateTaskApi(taskId: String, params: TaskUpdateParams): Result<TaskModel>

    suspend fun deleteTask(task: TaskModel): Result<Int>

    suspend fun deleteTaskApi(taskId: String): Result<Boolean>
}
