package com.sebastiannarvaez.todoappofflinefirst.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SidebarView(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .width(80.dp)
            .padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = {}) {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(34.dp)
            )
        }

        Spacer(Modifier.weight(1f))

        Column(verticalArrangement = Arrangement.spacedBy(80.dp)) {
            Text(
                text = "Expenses",
                modifier = Modifier.rotate(-90f),
                color = MaterialTheme.colorScheme.onSecondary,
                fontWeight = FontWeight.Light
            )

            Text(
                text = "Calendar",
                modifier = Modifier.rotate(-90f),
                color = MaterialTheme.colorScheme.onSecondary,
                fontWeight = FontWeight.Light
            )

            Text(
                text = "Todo List",
                modifier = Modifier.rotate(-90f),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.weight(1f))

        IconButton(onClick = {}) {
            Icon(
                Icons.Default.Settings,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}