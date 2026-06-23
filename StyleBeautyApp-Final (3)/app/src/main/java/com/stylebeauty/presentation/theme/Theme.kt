package com.stylebeauty.assistant.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Brand Colors
private val Pink80 = Color(0xFFFFB4D5)
private val PinkPrimary = Color(0xFFFF4081)
private val PinkDark = Color(0xFFC60055)
private val Purple40 = Color(0xFF6650a4)
private val PurpleGrey40 = Color(0xFF625b71)
private val Pink40 = Color(0xFF7D5260)
private val RoseGold = Color(0xFFB76E79)
private val Cream = Color(0xFFFFF5E1)
private val SoftPeach = Color(0xFFFFE5D9)

// Light Theme
private val LightColorScheme = lightColorScheme(
    primary = PinkPrimary,
    onPrimary = Color.White,
    primaryContainer = SoftPeach,
    onPrimaryContainer = PinkDark,
    secondary = RoseGold,
    onSecondary = Color.White,
    secondaryContainer = Cream,
    onSecondaryContainer = Purple40,
    tertiary = Pink40,
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    surface = Color.White,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFF3EDF7),
    onSurfaceVariant = Color(0xFF49454F),
    error = Color(0xFFB3261E),
    onError = Color.White
)

// Dark Theme
private val DarkColorScheme = darkColorScheme(
    primary = Pink80,
    onPrimary = PinkDark,
    primaryContainer = PinkDark,
    onPrimaryContainer = Pink80,
    secondary = RoseGold,
    onSecondary = Color(0xFF332D41),
    tertiary = Color(0xFFEFB8C8),
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410)
)

@Composable
fun StyleBeautyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
