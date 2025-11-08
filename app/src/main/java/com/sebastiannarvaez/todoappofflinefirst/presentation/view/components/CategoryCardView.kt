package com.sebastiannarvaez.todoappofflinefirst.presentation.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebastiannarvaez.todoappofflinefirst.presentation.viewmodel.TaskCategory

@Composable
fun CategoryCardView(
    category: TaskCategory,
    isSelected: Boolean,
    onPress: (category: TaskCategory?) -> Unit
) {
    Column(
        modifier = Modifier
            .defaultMinSize(minWidth = 140.dp)
            .shadow(
                elevation = 20.dp,
                RoundedCornerShape(10.dp),
            )
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.inverseOnSurface
            )
            .clickable { onPress(if (isSelected) null else category) }
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = category.icon)

        Text(category.displayName, fontWeight = FontWeight.SemiBold)

        Text(
            "6 tickets",
            fontWeight = FontWeight.Light,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}