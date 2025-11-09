package com.sebastiannarvaez.todoappofflinefirst.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskCreateRequest(
    val remoteId: String? = null,
    val localId: Long,
    val title: String,
    val description: String,
    val category: String,
    val isSynced: Boolean? = false,
    val isDeleted: Boolean? = false,
    @SerialName("is_check") val isChecked: Boolean? = false,
)

@Serializable
data class TaskUpdateRequest(
    val title: String? = null,
    val description: String? = null,
    val category: String? = null,
    val isSynced: Boolean? = null,
    val isDeleted: Boolean? = null,
    @SerialName("is_check") val isChecked: Boolean? = null,
)