package com.sebastiannarvaez.todoappofflinefirst.domain.usecase

import com.sebastiannarvaez.todoappofflinefirst.domain.models.TaskModel
import com.sebastiannarvaez.todoappofflinefirst.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    operator fun invoke(): Flow<List<TaskModel>> {
        return taskRepository.getAllTask()
    }
}