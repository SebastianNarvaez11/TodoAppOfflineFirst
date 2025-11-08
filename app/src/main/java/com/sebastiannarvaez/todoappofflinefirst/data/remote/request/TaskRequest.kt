package com.sebastiannarvaez.todoappofflinefirst.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskCreateRequest(
    val title: String,
    val description: String,
    val category: String,
    @SerialName("is_check") val isCheck: Boolean
)

@Serializable
data class TaskUpdateRequest(
    val title: String? = null,
    val description: String? = null,
    val category: String? = null,
    @SerialName("is_check") val isCheck: Boolean? = null
)