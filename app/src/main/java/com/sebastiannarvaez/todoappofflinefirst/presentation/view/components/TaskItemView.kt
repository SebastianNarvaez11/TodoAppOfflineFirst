package com.sebastiannarvaez.todoappofflinefirst.presentation.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebastiannarvaez.todoappofflinefirst.domain.models.TaskModel

@Composable
fun TaskItemView(
    task: TaskModel,
    onDelete: (task: TaskModel) -> Unit,
    onPress: (task: TaskModel) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPress(task) }
            .padding(end = 10.dp)) {

        Checkbox(checked = task.isDone, onCheckedChange = { onPress(task) })

        Column {
            Text(
                text = task.category.icon + " " + task.title,
                color = if (task.isDone) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onBackground,
                textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None
            )

            Text(
                text = task.description,
                color = MaterialTheme.colorScheme.onSecondary,
                textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None,
                fontSize = 10.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { onDelete(task) }) {
            Icon(
                Icons.Default.Clear,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}