package com.sebastiannarvaez.todoappofflinefirst.domain.usecase

import com.sebastiannarvaez.todoappofflinefirst.domain.models.TaskModel
import com.sebastiannarvaez.todoappofflinefirst.domain.models.TaskUpdateParams
import com.sebastiannarvaez.todoappofflinefirst.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskApiUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskId: String, params: TaskUpdateParams): Result<TaskModel> {
        return taskRepository.updateTaskApi(taskId, params)
    }
}