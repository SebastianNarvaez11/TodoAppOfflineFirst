package com.sebastiannarvaez.todoappofflinefirst.domain.usecase

import com.sebastiannarvaez.todoappofflinefirst.domain.models.TaskModel
import com.sebastiannarvaez.todoappofflinefirst.domain.repository.TaskRepository
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: TaskModel): Result<Long> {
        return taskRepository.createTask(task)
    }
}