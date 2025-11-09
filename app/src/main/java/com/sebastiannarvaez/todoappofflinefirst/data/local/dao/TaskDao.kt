package com.sebastiannarvaez.todoappofflinefirst.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sebastiannarvaez.todoappofflinefirst.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE isDeleted = 0 ORDER BY lastModified DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<TaskEntity>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createTask(task: TaskEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTask(task: TaskEntity): Int

    @Delete
    suspend fun deleteTask(task: TaskEntity): Int

    // NUEVOS MÉTODOS DE BÚSQUEDA
    @Query("SELECT * FROM tasks WHERE localId = :id")
    suspend fun getTaskByLocalId(id: Long): TaskEntity?

    @Query("SELECT * FROM tasks WHERE remoteId = :id")
    suspend fun getTaskByRemoteId(id: String): TaskEntity?

    // MÉTODOS DE ESCRITURA (Usar REPLACE)
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertOrUpdateTask(task: TaskEntity): Long

    // MÉTODOS DE SINCRONIZACIÓN
    @Query("SELECT * FROM tasks WHERE isSynced = 0")
    suspend fun getPendingTasks(): List<TaskEntity>

    @Query("DELETE FROM tasks WHERE localId = :localId")
    suspend fun hardDelete(localId: Long)
}