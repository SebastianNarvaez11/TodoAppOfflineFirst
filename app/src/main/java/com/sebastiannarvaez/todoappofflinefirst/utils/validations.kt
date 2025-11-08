package com.sebastiannarvaez.todoappofflinefirst.utils

fun validateTitle(title: String): String? {
    return when {
        title.trim() .isEmpty() -> "El titulo es requerido"
        title.trim().length < 3 -> "Minimo 3 caracteres"
        title.trim().length > 15 -> "Maximo 15 caracteres"
        else -> null
    }
}

fun validateDescription(description: String): String? {
    return when {
        description.trim().isEmpty() -> "La description es requerida"
        description.trim().length < 5 -> "Minimo 5 caracteres"
        description.trim().length > 20 -> "Maximo 20 caracteres"
        else -> null
    }
}