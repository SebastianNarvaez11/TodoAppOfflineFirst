package com.sebastiannarvaez.todoappofflinefirst.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sebastiannarvaez.todoappofflinefirst.presentation.viewmodel.TaskCategory

@Composable
fun CategoryItemView(
    isSelected: Boolean,
    category: TaskCategory,
    onPress: (category: TaskCategory) -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer)
            .clickable { onPress(category) }
            .padding(5.dp)
    ) {
        Text(
            text = category.icon + " " + category.displayName,
            color = if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onBackground
        )
    }
}
