package com.sebastiannarvaez.todoappofflinefirst.data.local.entity

import androidx.room.TypeConverter
import com.sebastiannarvaez.todoappofflinefirst.presentation.viewmodel.TaskCategory

class TaskCategoryConverter {
    // Convierte el TaskCategory a String para guardar en la BD
    @TypeConverter
    fun fromTaskCategory(category: TaskCategory): String {
        return category.name
    }

    // Convierte el String de la BD a TaskCategory para usar en Kotlin
    @TypeConverter
    fun toTaskCategory(categoryString: String): TaskCategory {
        return TaskCategory.valueOf(categoryString)
    }
}