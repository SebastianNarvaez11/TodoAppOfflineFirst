package com.sebastiannarvaez.todoappofflinefirst.presentation.viewmodel

enum class TaskCategory(val displayName: String, val icon: String) {
    SUBSCRIPTIONS("Suscripciones", "💳"),
    OUTINGS("Salidas", "🍻"),
    SERVICES("Servicios", "💡"),
    BIKE("Moto", "🏍️"),
    HELP_FAMILY("Familia", "👨‍👩‍👧"),
    GYM("Gym y suplementos", "💪"),
    HOME("Hogar", "🏠"),
    GROCERY_SHOP("Mercado", "🛒"),
    BARBER("Barbería", "💈"),
    SELF_TREATS("Personales", "🛍️"), // ropa, tecnología, cosas personales
    OTHER("Otros", "📦"),
}