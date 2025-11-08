package com.sebastiannarvaez.todoappofflinefirst.presentation.view.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sebastiannarvaez.todoappofflinefirst.presentation.viewmodel.TaskCategory

@Composable
fun CategoryListView(
    selectedCategory: TaskCategory?,
    onPressCategory: (category: TaskCategory?) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(TaskCategory.entries, key = { it.name }) { category ->
            CategoryCardView(category = category, isSelected = category == selectedCategory) {
                onPressCategory(it)
            }
        }
    }

}