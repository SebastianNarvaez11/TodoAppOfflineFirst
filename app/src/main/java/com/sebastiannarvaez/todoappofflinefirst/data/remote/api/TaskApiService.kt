package com.sebastiannarvaez.todoappofflinefirst.data.remote.api

import com.sebastiannarvaez.todoappofflinefirst.data.remote.request.TaskCreateRequest
import com.sebastiannarvaez.todoappofflinefirst.data.remote.request.TaskUpdateRequest
import com.sebastiannarvaez.todoappofflinefirst.data.remote.response.TaskDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface TaskApiService {
    @GET("/rest/v1/tasks?select=*")
    suspend fun getAllTask(): List<TaskDto>

    @POST("/rest/v1/tasks")
    @Headers("Prefer: return=representation")
    suspend fun createTask(@Body task: TaskCreateRequest): List<TaskDto>

    @PATCH("/rest/v1/tasks")
    @Headers("Prefer: return=representation")
    suspend fun updateTask(
        @Query(value = "id", encoded = true) filter: String,
        @Body task: TaskUpdateRequest
    ): List<TaskDto>

    @DELETE("/rest/v1/tasks")
    suspend fun  deleteTask(@Query(value = "id", encoded = true) filter: String)
}