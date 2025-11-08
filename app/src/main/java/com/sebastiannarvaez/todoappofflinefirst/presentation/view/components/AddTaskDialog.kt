package com.sebastiannarvaez.todoappofflinefirst.presentation.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sebastiannarvaez.todoappofflinefirst.presentation.viewmodel.TaskCategory
import com.sebastiannarvaez.todoappofflinefirst.presentation.viewmodel.TaskFormState

@Composable
fun AddTaskDialog(
    formState: TaskFormState,
    isSavingTask: Boolean,
    errorSavingTask: String?,
    onTitleChange: (value: String) -> Unit,
    onDescriptionChange: (value: String) -> Unit,
    onCategoryChange: (category: TaskCategory) -> Unit,
    onSavePress: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Text(text = "Add task")

            OutlinedTextField(
                value = formState.title.value,
                onValueChange = { onTitleChange(it) },
                isError = formState.title.error != null,
                supportingText = {
                    formState.title.error?.let { it ->
                        Text(text = it)
                    }
                },
                label = {
                    Text(
                        text = "Title", color = MaterialTheme.colorScheme.onSecondary
                    )
                })

            OutlinedTextField(
                value = formState.description.value,
                onValueChange = { onDescriptionChange(it) },
                isError = formState.description.error != null,
                supportingText = {
                    formState.description.error?.let { it ->
                        Text(text = it)
                    }
                },
                label = {
                    Text(
                        text = "Description", color = MaterialTheme.colorScheme.onSecondary
                    )
                })

            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(TaskCategory.entries, key = { it -> it.name }) { category ->
                    CategoryItemView(
                        isSelected = formState.category == category, category = category
                    ) {
                        onCategoryChange(it)
                    }
                }
            }

            Button(
                onClick = { onSavePress() },
                enabled = !isSavingTask,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isSavingTask) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp))
                } else {
                    Text(text = "Save")
                }
            }

            AnimatedVisibility(visible = errorSavingTask != null) {
                Text(text = errorSavingTask!!, color = MaterialTheme.colorScheme.onError)
            }
        }
    }
}