package com.sebastiannarvaez.todoappofflinefirst.domain.models

sealed class DomainError : Exception() {
    data class ApiError(override val message: String) : DomainError()
    data class NetworkError(override val message: String) : DomainError()
    data class DatabaseError(override val message: String) : DomainError()
    data class UnknownError(override val message: String) : DomainError()
}

