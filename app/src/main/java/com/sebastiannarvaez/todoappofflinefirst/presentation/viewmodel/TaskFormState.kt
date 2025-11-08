package com.sebastiannarvaez.todoappofflinefirst.presentation.viewmodel

import com.sebastiannarvaez.todoappofflinefirst.utils.validateTitle

data class TaskFormState(
    val title: FieldState = FieldState(),
    val description: FieldState = FieldState(),
    val category: TaskCategory? = null
) {
    val isValidForm: Boolean
        get() = title.error == null && description.error == null && category != null

    fun getErrors(): List<String> {
        val errors: MutableList<String> = mutableListOf()
        if (title.error != null) errors.add(title.error)
        if (description.error != null) errors.add(description.error)
        if (category == null) errors.add("Selecciona una categoria")
        return errors.toList()
    }
}
