package com.sebastiannarvaez.todoappofflinefirst.presentation.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebastiannarvaez.todoappofflinefirst.domain.models.TaskModel

@Composable
fun MainContainerView(
    innerPadding: PaddingValues,
    tasks: List<TaskModel>,
    isLoading: Boolean,
    error: String?,
    onLoadTaskFromApi: () -> Unit,
    onDeleteTask: (task: TaskModel) -> Unit,
    onPressTask: (task: TaskModel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Hi, welcome",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "29 NOV 2025",
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(160.dp))

        if (error != null) {
            Text(text = error, color = MaterialTheme.colorScheme.onError)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Todo List:", fontSize = 18.sp, fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(visible = isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp))
            }
        }

        Button(onClick = { onLoadTaskFromApi() }) {
            Text(text = "Cargar tasks desde la api")
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(tasks, key = { it.id }) { task ->
                TaskItemView(task = task, onDelete = { onDeleteTask(task) }) { task ->
                    onPressTask(task)
                }
            }
        }
    }
}