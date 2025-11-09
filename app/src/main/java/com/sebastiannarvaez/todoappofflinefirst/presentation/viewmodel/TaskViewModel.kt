package com.sebastiannarvaez.todoappofflinefirst.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebastiannarvaez.todoappofflinefirst.domain.usecase.GetAllTaskUseCase
import com.sebastiannarvaez.todoappofflinefirst.domain.models.TaskModel
import com.sebastiannarvaez.todoappofflinefirst.domain.models.TaskUpdateParams
import com.sebastiannarvaez.todoappofflinefirst.domain.usecase.CreateTaskApiUseCase
import com.sebastiannarvaez.todoappofflinefirst.domain.usecase.CreateTaskUseCase
import com.sebastiannarvaez.todoappofflinefirst.domain.usecase.DeleteTaskApiUseCase
import com.sebastiannarvaez.todoappofflinefirst.domain.usecase.DeleteTaskUseCase
import com.sebastiannarvaez.todoappofflinefirst.domain.usecase.GetAllTaskApiUseCase
import com.sebastiannarvaez.todoappofflinefirst.domain.usecase.RefreshFromRemoteUseCase
import com.sebastiannarvaez.todoappofflinefirst.domain.usecase.SyncPendingTasksUseCase
import com.sebastiannarvaez.todoappofflinefirst.domain.usecase.UpdateTaskApiUseCase
import com.sebastiannarvaez.todoappofflinefirst.domain.usecase.UpdateTaskUseCase
import com.sebastiannarvaez.todoappofflinefirst.utils.validateDescription
import com.sebastiannarvaez.todoappofflinefirst.utils.validateTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getAllTaskUseCase: GetAllTaskUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val refreshFromRemoteUseCase: RefreshFromRemoteUseCase,
    private val syncPendingTasksUseCase: SyncPendingTasksUseCase
//    private val deleteTaskUseCase: DeleteTaskUseCase,
//    private val getAllTaskApiUseCase: GetAllTaskApiUseCase,
//    private val createTaskApiUseCase: CreateTaskApiUseCase,
//    private val updateTaskApiUseCase: UpdateTaskApiUseCase,
//    private val deleteTaskApiUseCase: DeleteTaskApiUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEventState>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var _formState by mutableStateOf(TaskFormState())
    val formState: TaskFormState get() = _formState

    // 💡 _taskSourceFlow: FUENTE DE VERDAD DESDE LA BASE DE DATOS
    // Esto solo INSTANCIA el Flow, NO lo colecta. Es la "tubería viva"
    // y reactiva que Room actualiza automáticamente ante cambios en la tabla 'tasks'.
    // Su valor no es manipulado por el ViewModel, solo se OBSERVA.
    private val _taskSourceFlow: Flow<List<TaskModel>> = getAllTaskUseCase()

    // 💡 _filterCategory: FUENTE DE VERDAD DEL ESTADO DE LA UI
    // Es un MutableStateFlow para que el ViewModel tenga control total sobre su valor.
    // Al ser Mutable, podemos cambiar su valor con 'update' o '.value ='
    // cuando el usuario selecciona un filtro, disparando el 'combine'.
    private var _filterCategory = MutableStateFlow<TaskCategory?>(null)

    // 💡 init: PUNTO DE ACTIVACIÓN DE LA REACTIVIDAD
    init {
        // Al llamar a esta función en el init, aseguramos que la corrutina
        // con el '.collect' se lance tan pronto como el ViewModel está listo.
        // Esto le dice a Room "Empieza a vigilar la tabla ahora".
//        observeTask() // Versión simple, sin filtro.
        observerCombineTask() // Versión avanzada, combina datos + filtro.

        startInitialSync()
    }

    fun toggleAddTaskDialog() {
        _uiState.update {
            it.copy(
                visibleAddTaskDialog = !it.visibleAddTaskDialog,
                errorSavingTask = null
            )
        }
    }

    //FORM
    fun onTitleChange(title: String) {
        _formState = _formState.copy(
            title = _formState.title.copy(
                value = title,
                error = validateTitle(title)
            )
        )
    }

    fun onDescriptionChange(description: String) {
        _formState = _formState.copy(
            description = _formState.description.copy(
                value = description,
                error = validateDescription(description)
            )
        )
    }

    fun onCategoryChange(category: TaskCategory) {
        _formState = _formState.copy(category = category)
    }

    fun resetForm() {
        _formState = TaskFormState()
    }

    fun validateForm() {
        onTitleChange(_formState.title.value)
        onDescriptionChange(_formState.description.value)
    }

    //TASK ACTIONS

    // 💡 observerCombineTask: CREACIÓN DEL ESTADO FINAL (SINGLE SOURCE OF TRUTH)
    // Combina dos fuentes reactivas:
    // 1. _taskSourceFlow (cambios en la DB)
    // 2. _filterCategory (cambios en el filtro seleccionado por el usuario)
    // Este Flow se ejecuta cada vez que cualquiera de las dos fuentes emite un nuevo valor.
    fun observerCombineTask() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            _taskSourceFlow.combine(_filterCategory) { tasks, category ->
                val filteredTask =
                    category?.let { tasks.filter { it.category == category } } ?: tasks

                _uiState.value.copy(
                    tasks = filteredTask,
                    filterSelectedCategory = category,
                    isLoading = false,
                    error = null
                )
            }.catch { e ->
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Error al cargar las tareas",
                        isLoading = false
                    )
                }
            }.collect { newState -> _uiState.update { newState } }
        }
    }

    // 💡 observeTask: OBSERVACIÓN SIMPLE (OBSERVACIÓN DE UN SOLO FLOW)
    // Este es el método si solo estuvieras observando la lista de tareas SIN FILTRO.
    // Solo se activa si la DB cambia. Si se usara 'combine', sería innecesario.
    // (Mantengo la función aquí solo como referencia de un patrón anterior).
    fun observeTask() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            getAllTaskUseCase()
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            error = e.message ?: "Error al cargar las tareas",
                            isLoading = false
                        )
                    }
                }
                .collect { newTaskList ->
                    _uiState.update {
                        it.copy(
                            tasks = newTaskList,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun onSelectFilterCategory(category: TaskCategory?) {
        // Actualizamos el MutableStateFlow, lo cual dispara una nueva ejecución del 'combine'
        _filterCategory.update { category }
    }

    fun onSaveTask() {
        validateForm()

        if (_formState.isValidForm) {
            val newTask = TaskModel(
                localId = 0,
                title = _formState.title.value,
                description = _formState.description.value,
                category = _formState.category!!,
                isDone = false,
            )

            _uiState.update { it.copy(isSavingTask = true) }

            viewModelScope.launch {
//                delay(2000)
                createTaskUseCase(newTask)
                    .onSuccess {
                        _uiState.update { it.copy(isSavingTask = false, errorSavingTask = null) }
                        _uiEvent.emit(UiEventState.ShowToast("Tarea creada"))
                        toggleAddTaskDialog()
                        resetForm()
                    }
                    .onFailure { e ->
                        _uiState.update {
                            it.copy(
                                isSavingTask = false,
                                errorSavingTask = e.message ?: "Error al crear la tarea"
                            )
                        }
                    }
            }
        } else {
            val errors = _formState.getErrors()
            if (errors.isNotEmpty()) {
                viewModelScope.launch {
                    _uiEvent.emit(UiEventState.ShowToast(errors[0]))
                }
            }
        }
    }

    fun toggleTaskStatus(task: TaskModel) {
        _uiState.update { it.copy(isUpdatingTask = true) }

        viewModelScope.launch {
            updateTaskUseCase(task.localId, TaskUpdateParams(isDone = !task.isDone))
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message) }
                }

            _uiState.update { it.copy(isUpdatingTask = false) }
        }
    }

    fun deleteTask(task: TaskModel) {
        _uiState.update { it.copy(isUpdatingTask = true) }

        viewModelScope.launch {
            updateTaskUseCase(task.localId, TaskUpdateParams(isDeleted = true))
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message) }
                }

            _uiState.update { it.copy(isUpdatingTask = false) }

//            deleteTaskUseCase(task)
//                .onSuccess { _uiEvent.emit(UiEventState.ShowToast("Tarea eliminada")) }
//                .onFailure { e -> _uiState.update { it.copy(error = e.message) } }
        }
    }

//    fun getAllTaskFromApi() {
//        _uiState.update { it.copy(isLoading = true) }
//
//        viewModelScope.launch {
//            getAllTaskApiUseCase()
//                .onSuccess { tasks ->
//                    _uiState.update {
//                        it.copy(
//                            tasks = tasks,
//                            isLoading = false
//                        )
//                    }
//                }
//                .onFailure { e ->
//                    _uiState.update {
//                        it.copy(
//                            isLoading = false,
//                            error = e.message ?: "Error al obtener las task de la api"
//                        )
//                    }
//                }
//        }
//    }

//    fun onSaveTaskApi() {
//        validateForm()
//
//        if (_formState.isValidForm) {
//            val newTask = TaskModel(
//                id = "0",
//                title = _formState.title.value,
//                description = _formState.description.value,
//                category = _formState.category!!,
//                isDone = false
//            )
//
//            _uiState.update { it.copy(isSavingTask = true) }
//
//            viewModelScope.launch {
//                delay(2000)
//                createTaskApiUseCase(newTask)
//                    .onSuccess { createdTask ->
//                        _uiState.update { it.copy(isSavingTask = false, errorSavingTask = null) }
//                        _uiEvent.emit(UiEventState.ShowToast("Tarea creada con el id: ${createdTask.id}"))
//                        toggleAddTaskDialog()
//                        resetForm()
//                        getAllTaskFromApi()//refrescar la lista manualmente
//                    }
//                    .onFailure { e ->
//                        println(e)
//                        _uiState.update {
//                            it.copy(
//                                isSavingTask = false,
//                                errorSavingTask = e.message ?: "Error al crear la tarea"
//                            )
//                        }
//                    }
//            }
//        } else {
//            val errors = _formState.getErrors()
//            if (errors.isNotEmpty()) {
//                viewModelScope.launch {
//                    _uiEvent.emit(UiEventState.ShowToast(errors[0]))
//                }
//            }
//        }
//    }

//    fun toggleTaskStatusApi(task: TaskModel) {
//        _uiState.update { it.copy(isUpdatingTask = true) }
//
//        viewModelScope.launch {
//            val paramsToUpdate = TaskUpdateParams(isDone = !task.isDone)
//            updateTaskApiUseCase(task.id, paramsToUpdate)
//                .onSuccess { updatedTask ->
//                    _uiState.update { it.copy(isUpdatingTask = false) }
//                    getAllTaskFromApi()//refrescar la lista manualmente
//                }
//                .onFailure { e ->
//                    _uiState.update {
//                        it.copy(
//                            error = e.message ?: "Error al actualizar",
//                            isUpdatingTask = false
//                        )
//                    }
//                }
//        }
//    }

//    fun deleteTaskApi(task: TaskModel) {
//        viewModelScope.launch {
//            deleteTaskApiUseCase(task.id)
//                .onSuccess {
//                    _uiEvent.emit(UiEventState.ShowToast("Tarea eliminada"))
//                    getAllTaskFromApi()//refrescar la lista manualmente
//                }
//                .onFailure { e ->
//                    _uiState.update {
//                        it.copy(
//                            error = e.message ?: "Error al borrar tarea"
//                        )
//                    }
//                }
//        }
//    }

//    private fun refreshTasksFromRemoteOnStart() {
//        _uiState.update { it.copy(isRefreshingFromRemote = true) }
//
//        viewModelScope.launch {
//            delay(2000)
//            refreshFromRemoteUseCase() // Esto llena Room
//                .onFailure { e ->
//                    // El ViewModel maneja el error de "refresco"
//                    _uiState.update { it.copy(error = "No se puedo refrecar: ${e.message}") }
//                }
//            // No necesitamos .onSuccess, porque el Flow 'observerCombineTask'
//            // detectará los cambios en Room y actualizará la UI solo.
//            _uiState.update { it.copy(isRefreshingFromRemote = false) }
//        }
//    }
//
//    private fun syncPendingTasks() {
//        _uiState.update { it.copy(isRefreshingFromRemote = true) }
//
//        viewModelScope.launch {
//            syncPendingTasksUseCase()
//                .onFailure { e ->
//                    // El ViewModel maneja el error de "sincronizacion"
//                    _uiState.update { it.copy(error = "No se puedo sincronizar: ${e.message}") }
//                }
//
//            // No necesitamos .onSuccess, porque el Flow 'observerCombineTask'
//            // detectará los cambios en Room y actualizará la UI solo.
//            _uiState.update { it.copy(isRefreshingFromRemote = false) }
//        }
//    }

    /**
     * Inicia la secuencia de sincronización completa (PUSH-then-PULL).
     * Esta función es "segura" para llamarse desde el 'init'
     * o desde un botón de refresco manual.
     */
    fun startInitialSync() {
        // Lanzamos una sola corutina para toda la secuencia
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshingFromRemote = true) } // Mostrar spinner

            // 1. PUSH: Intentar subir cambios locales primero
            val pushResult = syncPendingTasksUseCase()

            if (pushResult.isFailure) {
                // Opcional: Notificar al usuario que el PUSH falló
                // El PULL se ejecutará de todos modos para obtener datos nuevos
                _uiState.update { it.copy(error = "Error al subir cambios: ${pushResult.exceptionOrNull()?.message}") }
            }

            // 2. PULL: Traer cambios del servidor
            val pullResult = refreshFromRemoteUseCase()

            if (pullResult.isFailure) {
                _uiState.update { it.copy(error = "Error al descargar tareas: ${pushResult.exceptionOrNull()?.message}") }
            }

            // 3. Ocultar spinner
            _uiState.update { it.copy(isRefreshingFromRemote = false) }

            if (pushResult.isSuccess && pullResult.isSuccess) {
                _uiEvent.emit(UiEventState.ShowToast("Datos sincronizados ✅"))
            }
        }
    }
}