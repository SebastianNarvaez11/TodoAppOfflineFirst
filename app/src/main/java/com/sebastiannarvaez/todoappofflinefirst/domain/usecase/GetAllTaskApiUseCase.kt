package com.sebastiannarvaez.todoappofflinefirst.domain.usecase

import com.sebastiannarvaez.todoappofflinefirst.domain.models.TaskModel
import com.sebastiannarvaez.todoappofflinefirst.domain.repository.TaskRepository
import javax.inject.Inject

class GetAllTaskApiUseCase @Inject constructor(private val taskRepository: TaskRepository) {
//    suspend operator fun invoke(): Result<List<TaskModel>> {
//        return taskRepository.getAllTaskApi()
//    }
}