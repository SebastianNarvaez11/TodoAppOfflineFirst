package com.sebastiannarvaez.todoappofflinefirst.presentation.viewmodel

sealed class UiEventState() {
    data class ShowToast(val message: String): UiEventState()
}
