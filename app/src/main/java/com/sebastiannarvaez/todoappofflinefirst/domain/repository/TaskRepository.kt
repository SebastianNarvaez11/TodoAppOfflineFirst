package com.sebastiannarvaez.todoappofflinefirst.domain.repository

import com.sebastiannarvaez.todoappofflinefirst.domain.models.TaskModel
import com.sebastiannarvaez.todoappofflinefirst.domain.models.TaskUpdateParams
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTask(): Flow<List<TaskModel>>

    suspend fun refreshFromRemote(): Result<Unit>

    suspend fun syncPendingTasks(): Result<Unit>

//    suspend fun getAllTaskApi(): Result<List<TaskModel>>

    suspend fun createTask(task: TaskModel): Result<Unit>

//    suspend fun createTaskApi(task: TaskModel): Result<TaskModel>

    suspend fun updateTask(taskLocalId: Long, params: TaskUpdateParams): Result<Unit>

//    suspend fun updateTaskApi(taskId: String, params: TaskUpdateParams): Result<TaskModel>

//    suspend fun deleteTask(task: TaskModel): Result<Unit>

//    suspend fun deleteTaskApi(taskId: String): Result<Boolean>
}
