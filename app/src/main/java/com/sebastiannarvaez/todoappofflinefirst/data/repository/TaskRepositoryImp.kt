package com.sebastiannarvaez.todoappofflinefirst.data.repository

import android.util.Log
import com.sebastiannarvaez.todoappofflinefirst.data.local.dao.TaskDao
import com.sebastiannarvaez.todoappofflinefirst.data.local.entity.TaskEntity
import com.sebastiannarvaez.todoappofflinefirst.data.remote.api.TaskApiService
import com.sebastiannarvaez.todoappofflinefirst.data.remote.request.TaskUpdateRequest
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

    override suspend fun refreshFromRemote(): Result<Unit> {
        try {
            val remoteTask = taskApi.getAllTask()
            val remoteTasksToEntities =
                remoteTask.map { it.toDomain().toEntity().copy(isSynced = true) }
            taskDao.insertAll(remoteTasksToEntities)

            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(errorMapper.map(e))
        }
    }

    override suspend fun syncPendingTasks(): Result<Unit> {
        return try {
            val pendingTask = taskDao.getPendingTasks()

            for (localTask in pendingTask) {
                try {
                    //sincronizar borrados
                    if (localTask.isDeleted) {
                        if (localTask.remoteId != null) {
                            val filter = "eq.${localTask.remoteId}"
                            taskApi.updateTask(
                                filter,
                                TaskUpdateRequest(isDeleted = true, isSynced = true)
                            )
                        }

                        taskDao.hardDelete(localTask.localId)
                    } else if (localTask.remoteId == null) {
                        //sincronizar creaciones
                        val crestedTaskRequest = taskApi.createTask(
                            localTask.toDomain().toTaskCreateRequest().copy(isSynced = true)
                        )[0]
                        taskDao.updateTask(crestedTaskRequest.toDomain().toEntity())
                    } else {
                        //sincronizar actualizaciones
                        val filter = "eq.${localTask.remoteId}"
                        val taskUpdateRequest =
                            localTask.toDomain().toTaskUpdateRequest().copy(isSynced = true)

                        val taskUpdatedResponse = taskApi.updateTask(filter, taskUpdateRequest)[0]
                        taskDao.updateTask(taskUpdatedResponse.toDomain().toEntity())
                    }
                } catch (e: Exception) {
                    continue
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(errorMapper.map(e))
        }
    }

//    override suspend fun getAllTaskApi(): Result<List<TaskModel>> {
//        try {
//            val result = taskApi.getAllTask()
//            return Result.success(result.map { it.toDomain() })
//        } catch (e: Exception) {
//            return Result.failure(errorMapper.map(e))
//        }
//    }

    override suspend fun createTask(task: TaskModel): Result<Unit> {
        try {
            val taskLocalId = taskDao.createTask(task.toEntity())

            runCatching {
                val createTaskRequest =
                    task.toTaskCreateRequest().copy(localId = taskLocalId, isSynced = true)

                val createTaskResponse = taskApi.createTask(createTaskRequest)[0]

                taskDao.updateTask(createTaskResponse.toDomain().toEntity())
            }

            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(errorMapper.map(e))
        }
    }

//    override suspend fun createTaskApi(task: TaskModel): Result<TaskModel> {
//        try {
//            val result = taskApi.createTask(task.toTaskCreateRequest())
//            return Result.success(result[0].toDomain())
//        } catch (e: Exception) {
//            return Result.failure(errorMapper.map(e))
//        }
//    }

    override suspend fun updateTask(taskLocalId: Long, params: TaskUpdateParams): Result<Unit> {
        try {
            val localTask = taskDao.getTaskByLocalId(taskLocalId)

            if (localTask != null) {
                val updatedTaskEntity = TaskEntity(
                    localId = localTask.localId,
                    remoteId = localTask.remoteId,
                    title = params.title ?: localTask.title,
                    description = params.description ?: localTask.description,
                    category = params.category ?: localTask.category,
                    isDone = params.isDone ?: localTask.isDone,
                    isSynced = false,
                    isDeleted = params.isDeleted ?: localTask.isDeleted
                )

                taskDao.updateTask(updatedTaskEntity)

                runCatching {
                    if (localTask.remoteId != null) {
                        //actualizar
                        val updatedTaskRequest =
                            updatedTaskEntity.toDomain().toTaskUpdateRequest().copy(isSynced = true)
                        val filter = "eq.${localTask.remoteId}"
                        val updatedTaskResponse = taskApi.updateTask(filter, updatedTaskRequest)[0]
                        taskDao.updateTask(updatedTaskResponse.toDomain().toEntity())
                    } else {
                        //intentar crear
                        val createTaskRequest =
                            updatedTaskEntity.toDomain().toTaskCreateRequest().copy(isSynced = true)
                        val createTaskResponse = taskApi.createTask(createTaskRequest)[0]
                        taskDao.updateTask(createTaskResponse.toDomain().toEntity())
                    }
                }

                return Result.success(Unit)
            } else {
                throw Exception("No se encontro la tarea a actualizar")
            }
        } catch (e: Exception) {
            return Result.failure(errorMapper.map(e))
        }
    }

//    override suspend fun updateTaskApi(
//        taskId: String,
//        params: TaskUpdateParams
//    ): Result<TaskModel> {
//        val filter = "eq.$taskId"
//
//        try {
//            val result = taskApi.updateTask(filter, params.toTaskUpdateRequest())
//            return Result.success(result[0].toDomain())
//        } catch (e: Exception) {
//            return Result.failure(errorMapper.map(e))
//        }
//    }

//    override suspend fun deleteTask(task: TaskModel): Result<Unit> {
//        try {
//            taskDao.updateTask(task.toEntity().copy(isDeleted = true, isSynced = false))
//
//            runCatching {
//                val taskUpdateRequest = TaskUpdateRequest(isDeleted = true, isSynced = true)
//                val taskUpdatedResponse = taskApi.updateTask()
//            }
//
//
//            return Result.success(result)
//        } catch (e: Exception) {
//            return Result.failure(errorMapper.map(e))
//        }
//    }

//    override suspend fun deleteTaskApi(taskId: String): Result<Boolean> {
//        val filter = "eq.$taskId"
//
//        try {
//            taskApi.deleteTask(filter)
//            return Result.success(true)
//        } catch (e: Exception) {
//            return Result.failure(errorMapper.map(e))
//        }
//    }
}