package com.sebastiannarvaez.todoappofflinefirst.data.repository

import com.sebastiannarvaez.todoappofflinefirst.data.local.dao.TaskDao
import com.sebastiannarvaez.todoappofflinefirst.data.remote.api.TaskApiService
import com.sebastiannarvaez.todoappofflinefirst.data.remote.response.toDomain
import com.sebastiannarvaez.todoappofflinefirst.data.utils.ErrorMapper
import com.sebastiannarvaez.todoappofflinefirst.domain.models.TaskModel
import com.sebastiannarvaez.todoappofflinefirst.domain.models.TaskUpdateParams
import com.sebastiannarvaez.todoappofflinefirst.domain.models.toEntity
import com.sebastiannarvaez.todoappofflinefirst.domain.models.toTaskCreateRequest
import com.sebastiannarvaez.todoappofflinefirst.domain.models.toTaskUpdateRequest
import com.sebastiannarvaez.todoappofflinefirst.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImp @Inject constructor(
    private val taskDao: TaskDao,
    private val taskApi: TaskApiService,
    private val errorMapper: ErrorMapper
) : TaskRepository {
    override fun getAllTask(): Flow<List<TaskModel>> {
        return taskDao.getAllTasks()
            .map { taskEntitiesList -> taskEntitiesList.map { it.toDomain() } }
    }

    override suspend fun getAllTaskApi(): Result<List<TaskModel>> {
        try {
            val result = taskApi.getAllTask()
            return Result.success(result.map { it.toDomain() })
        } catch (e: Exception) {
            return Result.failure(errorMapper.map(e))
        }
    }

    override suspend fun createTask(task: TaskModel): Result<Long> {
        try {
            val result = taskDao.createTask(task.toEntity())
            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(errorMapper.map(e))
        }
    }

    override suspend fun createTaskApi(task: TaskModel): Result<TaskModel> {
        try {
            val result = taskApi.createTask(task.toTaskCreateRequest())
            return Result.success(result[0].toDomain())
        } catch (e: Exception) {
            return Result.failure(errorMapper.map(e))
        }
    }

    override suspend fun updateTask(task: TaskModel): Result<Int> {
        try {
            val result = taskDao.updateTask(task.toEntity())
            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(errorMapper.map(e))
        }
    }

    override suspend fun updateTaskApi(
        taskId: String,
        params: TaskUpdateParams
    ): Result<TaskModel> {
        val filter = "eq.$taskId"

        try {
            val result = taskApi.updateTask(filter, params.toTaskUpdateRequest())
            return Result.success(result[0].toDomain())
        } catch (e: Exception) {
            return Result.failure(errorMapper.map(e))
        }
    }

    override suspend fun deleteTask(task: TaskModel): Result<Int> {
        try {
            val result = taskDao.deleteTask(task.toEntity())
            return Result.success(result)
        } catch (e: Exception) {
            return Result.failure(errorMapper.map(e))
        }
    }

    override suspend fun deleteTaskApi(taskId: String): Result<Boolean> {
        val filter = "eq.$taskId"

        try {
            taskApi.deleteTask(filter)
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(errorMapper.map(e))
        }
    }
}