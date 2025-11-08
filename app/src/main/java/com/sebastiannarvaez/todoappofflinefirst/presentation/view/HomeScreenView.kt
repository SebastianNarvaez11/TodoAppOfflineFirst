package com.sebastiannarvaez.todoappofflinefirst.presentation.view

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sebastiannarvaez.todoappofflinefirst.presentation.view.components.AddTaskDialog
import com.sebastiannarvaez.todoappofflinefirst.presentation.view.components.CategoryListView
import com.sebastiannarvaez.todoappofflinefirst.presentation.view.components.MainContainerView
import com.sebastiannarvaez.todoappofflinefirst.presentation.view.components.SidebarView
import com.sebastiannarvaez.todoappofflinefirst.presentation.viewmodel.TaskViewModel
import com.sebastiannarvaez.todoappofflinefirst.presentation.viewmodel.UiEventState

@Composable
fun HomeScreenView(innerPadding: PaddingValues, viewModel: TaskViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { value ->
            when (value) {
                is UiEventState.ShowToast -> Toast.makeText(
                    context,
                    value.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    Row {
        SidebarView(innerPadding = innerPadding)
        MainContainerView(
            innerPadding = innerPadding,
            tasks = uiState.tasks,
            error = uiState.error,
            isLoading = uiState.isLoading,
            onLoadTaskFromApi = { viewModel.getAllTaskFromApi() },
//            onDeleteTask = { viewModel.deleteTask(it) },
            onDeleteTask = { viewModel.deleteTaskApi(it) },
//            onPressTask = { viewModel.toggleTaskStatus(it) }
            onPressTask = { viewModel.toggleTaskStatusApi(it) }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
            .padding(top = 70.dp)
    ) {
        CategoryListView(
            selectedCategory = uiState.filterSelectedCategory,
            onPressCategory = { viewModel.onSelectFilterCategory(it) })
    }

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(end = 16.dp)
    ) {
        FloatingActionButton(
            onClick = { viewModel.toggleAddTaskDialog() },
            shape = RoundedCornerShape(30.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = "add")
        }
    }

    if (uiState.visibleAddTaskDialog) {
        AddTaskDialog(
            formState = viewModel.formState,
            isSavingTask = uiState.isSavingTask,
            errorSavingTask = uiState.errorSavingTask,
            onTitleChange = { viewModel.onTitleChange(it) },
            onDescriptionChange = { viewModel.onDescriptionChange(it) },
            onCategoryChange = { viewModel.onCategoryChange(it) },
//            onSavePress = { viewModel.onSaveTask() }
            onSavePress = { viewModel.onSaveTaskApi() }
        ) {
            viewModel.toggleAddTaskDialog()
            viewModel.resetForm()
        }
    }
}

