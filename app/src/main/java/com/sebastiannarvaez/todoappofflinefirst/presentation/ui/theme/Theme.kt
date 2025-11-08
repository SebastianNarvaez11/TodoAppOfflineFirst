package com.sebastiannarvaez.todoappofflinefirst.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Purple60,
    onPrimary = Gray95,
    primaryContainer = Purple90,
    onPrimaryContainer = Purple10,
    secondary = Lavender60,
    onSecondary = Lavender30,
    secondaryContainer = Lavender80,
    onSecondaryContainer = Lavender30,
    tertiary = Gray95,
    onTertiary = Pink20,
    tertiaryContainer = Pink80,
    onTertiaryContainer = Pink40,
    error = Red60,
    onError = Gray95,
    errorContainer = Red90,
    onErrorContainer = Red30,
    background = Gray95,
    onBackground = Gray20,
    surface = Gray90,
    onSurface = Gray20,
    surfaceVariant = Gray80,
    onSurfaceVariant = Gray40,
    outline = Gray40,
    inverseOnSurface = Gray95,
    inverseSurface = Gray30,
    inversePrimary = Purple80,
    surfaceTint = Purple60
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple60,
    onPrimary = Purple20,
    primaryContainer = Purple30,
    onPrimaryContainer = Purple90,
    secondary = Lavender60,
    onSecondary = Lavender30,
    secondaryContainer = Lavender30,
    onSecondaryContainer = Lavender80,
    tertiary = Gray95,
    onTertiary = Pink40,
    tertiaryContainer = Pink40,
    onTertiaryContainer = Pink80,
    error = Red80,
    onError = Red30,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = Gray5,
    onBackground = Gray80,
    surface = Gray10,
    onSurface = Gray80,
    surfaceVariant = Gray40,
    onSurfaceVariant = Gray80,
    outline = Gray40,
    inverseOnSurface = Gray20,
    inverseSurface = Gray80,
    inversePrimary = Purple60,
    surfaceTint = Purple80
)

@Composable
fun TodoAppOfflineFirstTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}