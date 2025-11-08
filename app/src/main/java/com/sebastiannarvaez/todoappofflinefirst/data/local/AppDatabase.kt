package com.sebastiannarvaez.todoappofflinefirst.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sebastiannarvaez.todoappofflinefirst.data.local.dao.TaskDao
import com.sebastiannarvaez.todoappofflinefirst.data.local.entity.TaskCategoryConverter
import com.sebastiannarvaez.todoappofflinefirst.data.local.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
@TypeConverters(TaskCategoryConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}