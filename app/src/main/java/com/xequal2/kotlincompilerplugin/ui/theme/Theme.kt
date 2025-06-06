package com.xequal2.kotlincompilerplugin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AccentBlue,
    secondary = AccentBlue,
    background = Black,
    surface = DarkGray,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
)

@Composable
fun KotlinCompilerPluginTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
