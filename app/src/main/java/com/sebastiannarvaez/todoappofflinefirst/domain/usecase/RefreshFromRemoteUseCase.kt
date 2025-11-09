package com.sebastiannarvaez.todoappofflinefirst.domain.usecase

import com.sebastiannarvaez.todoappofflinefirst.domain.repository.TaskRepository
import javax.inject.Inject

class RefreshFromRemoteUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return taskRepository.refreshFromRemote()
    }
}