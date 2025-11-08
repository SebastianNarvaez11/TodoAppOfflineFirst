package com.sebastiannarvaez.todoappofflinefirst.data.utils

import android.database.SQLException
import android.util.Log
import retrofit2.HttpException
import com.sebastiannarvaez.todoappofflinefirst.data.remote.response.SupabaseErrorResponse
import com.sebastiannarvaez.todoappofflinefirst.domain.models.DomainError
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

class ErrorMapper @Inject constructor(private val json: Json) {
    fun map(e: Exception): DomainError {
        return when (e) {
            is HttpException -> mapHttpError(e)
            is IOException -> {
                Log.e("NetworkErrorTag", null, e)
                DomainError.NetworkError(e.message ?: "Network error")
            }

            is SQLException -> {
                Log.e("DatabaseErrorTag", null, e)
                DomainError.DatabaseError(e.message ?: "Database error")
            }
            else -> {
                Log.e("UnknownErrorTag", null, e)
                DomainError.UnknownError(e.message ?: "Unknown error")
            }
        }
    }

    private fun mapHttpError(e: HttpException): DomainError.ApiError {
        val errorBody = e.response()?.errorBody()?.string()
        val errorResponse = json.decodeFromString<SupabaseErrorResponse>(errorBody ?: "")

        return try {
            Log.e("SupabaseErrorTag", errorBody ?: "")
            val message = errorResponse.message ?: "Error de api desconocido"
            val hint = errorResponse.hint?.let { "\nSugerencia: $it" } ?: ""

            DomainError.ApiError("$message. $hint")
        } catch (jsonError: Exception) {
            DomainError.ApiError("Error: ${jsonError.message}, No se pudo interpretar el error de Supabase.")
        }
    }
}