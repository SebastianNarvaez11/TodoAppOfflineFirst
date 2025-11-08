package com.sebastiannarvaez.todoappofflinefirst

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sebastiannarvaez.todoappofflinefirst.presentation.ui.theme.TodoAppOfflineFirstTheme
import com.sebastiannarvaez.todoappofflinefirst.presentation.view.HomeScreenView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoAppOfflineFirstTheme {
                Scaffold { innerPadding ->
                    LayoutScreen {
                        HomeScreenView(innerPadding = innerPadding)
                    }
                }
            }
        }
    }
}

@Composable
fun LayoutScreen(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoAppOfflineFirstTheme {
        LayoutScreen {
            HomeScreenView(innerPadding = PaddingValues(vertical = 16.dp))
        }
    }
}