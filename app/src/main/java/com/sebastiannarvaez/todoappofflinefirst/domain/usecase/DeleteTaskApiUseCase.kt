package com.sebastiannarvaez.todoappofflinefirst.domain.usecase

import com.sebastiannarvaez.todoappofflinefirst.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskApiUseCase @Inject constructor(private val taskRepository: TaskRepository) {
//    suspend operator fun invoke(taskId: String): Result<Boolean> {
//        return taskRepository.deleteTaskApi(taskId)
//    }
}