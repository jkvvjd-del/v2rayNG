package com.v2ray.ang.ui.compose

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.v2ray.ang.AppConfig
import com.v2ray.ang.handler.MmkvManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Style 2: light minimal
private val LightColor = lightColorScheme(
    primary = Color(0xFF2563EB),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDBE6FF),
    onPrimaryContainer = Color(0xFF0B1F52),
    secondary = Color(0xFF475569),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE2E8F0),
    onSecondaryContainer = Color(0xFF0F172A),
    tertiary = Color(0xFF059669),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFD1FAE5),
    onTertiaryContainer = Color(0xFF022C22),
    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFFDAD6),
    onError = Color(0xFFFFFFFF),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFF6F7F9),
    onBackground = Color(0xFF111827),
    surface = Color(0xFFF6F7F9),
    onSurface = Color(0xFF111827),
    surfaceVariant = Color(0xFFE5E7EB),
    onSurfaceVariant = Color(0xFF6B7280),
    outline = Color(0xFF9CA3AF),
    outlineVariant = Color(0xFFE5E7EB),
    inverseSurface = Color(0xFF1F2937),
    inverseOnSurface = Color(0xFFF3F4F6),
    inversePrimary = Color(0xFF93B4FF),
    scrim = Color(0xFF000000),
    surfaceTint = Color(0xFF2563EB),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFFAFBFC),
    surfaceContainer = Color(0xFFF1F3F6),
    surfaceContainerHigh = Color(0xFFEBEEF2),
    surfaceContainerHighest = Color(0xFFE5E8ED),
)

// Style 1: dark neon
private val DarkColor = darkColorScheme(
    primary = Color(0xFF00B4FF),
    onPrimary = Color(0xFF00212E),
    primaryContainer = Color(0xFF0B3A57),
    onPrimaryContainer = Color(0xFFD6F1FF),
    secondary = Color(0xFF00E5FF),
    onSecondary = Color(0xFF00363D),
    secondaryContainer = Color(0xFF0A3A44),
    onSecondaryContainer = Color(0xFFB8F5FF),
    tertiary = Color(0xFF7C5CFF),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF2B2260),
    onTertiaryContainer = Color(0xFFE4DDFF),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF070C18),
    onBackground = Color(0xFFD6F1FF),
    surface = Color(0xFF070C18),
    onSurface = Color(0xFFD6F1FF),
    surfaceVariant = Color(0xFF122338),
    onSurfaceVariant = Color(0xFF8FB0D0),
    outline = Color(0xFF5F89B0),
    outlineVariant = Color(0xFF1B3048),
    inverseSurface = Color(0xFFD6F1FF),
    inverseOnSurface = Color(0xFF070C18),
    inversePrimary = Color(0xFF006690),
    scrim = Color(0xFF000000),
    surfaceTint = Color(0xFF00B4FF),
    surfaceContainerLowest = Color(0xFF050912),
    surfaceContainerLow = Color(0xFF09111F),
    surfaceContainer = Color(0xFF0B1526),
    surfaceContainerHigh = Color(0xFF111E33),
    surfaceContainerHighest = Color(0xFF18283F),
)

// Style 3: Happ-like dark violet
private val HappColor = darkColorScheme(
    primary = Color(0xFF7C5CFF),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF2B2260),
    onPrimaryContainer = Color(0xFFE4DDFF),
    secondary = Color(0xFFB7A6FF),
    onSecondary = Color(0xFF2B1B6B),
    secondaryContainer = Color(0xFF241E36),
    onSecondaryContainer = Color(0xFFE4DDFF),
    tertiary = Color(0xFF5EE0B8),
    onTertiary = Color(0xFF00382E),
    tertiaryContainer = Color(0xFF005143),
    onTertiaryContainer = Color(0xFFA0F2D0),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF121018),
    onBackground = Color(0xFFF3F0FF),
    surface = Color(0xFF121018),
    onSurface = Color(0xFFF3F0FF),
    surfaceVariant = Color(0xFF2A2440),
    onSurfaceVariant = Color(0xFFB8B0D0),
    outline = Color(0xFF8E87A6),
    outlineVariant = Color(0xFF2D2644),
    inverseSurface = Color(0xFFF3F0FF),
    inverseOnSurface = Color(0xFF121018),
    inversePrimary = Color(0xFF5A3FD9),
    scrim = Color(0xFF000000),
    surfaceTint = Color(0xFF7C5CFF),
    surfaceContainerLowest = Color(0xFF0D0B12),
    surfaceContainerLow = Color(0xFF16131E),
    surfaceContainer = Color(0xFF1B1728),
    surfaceContainerHigh = Color(0xFF241E36),
    surfaceContainerHighest = Color(0xFF2D2644),
)

// Semantic Colors
val colorPing = Color(0xFF009966) // Green
val colorPingRed = Color(0xFFFF0099) // Pink Red
val colorConfigType = Color(0xFFf97910) // Orange
val colorFabActive = Color(0xFFf97910) // Orange
val colorFabInactiveLight = Color(0xFF9C9C9C) // Gray
val colorFabInactiveDark = Color(0xFF646464) // Dark Gray
val dividerColorLight = Color(0xFFE0E0E0) // Light Gray
val dividerColorDark = Color(0xFF424242) // Dark Gray

// Toast Colors 85%
val toastNormalBgLight = Color(0xD9353A3E) // Dark Gray
val toastNormalBgDark = Color(0xD94A4F54) // Darker Gray
val toastSuccessBg = Color(0xD9388E3C) // Green
val toastErrorBg = Color(0xD9D50000) // Red
val toastInfoBg = Color(0xD93F51B5) // Indigo Blue
val toastIconCircleBg = Color(0x33FFFFFF) // Semi-transparent White
val toastTextColor = Color.White // White

object ThemeManager {
    private val _themeMode = MutableStateFlow(
        MmkvManager.decodeSettingsString(AppConfig.PREF_UI_MODE_NIGHT, "0") ?: "0"
    )
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    private val _dynamicColorEnabled = MutableStateFlow(
        MmkvManager.decodeSettingsBool(AppConfig.PREF_DYNAMIC_COLOR, true)
    )
    val dynamicColorEnabled: StateFlow<Boolean> = _dynamicColorEnabled.asStateFlow()

    fun setThemeMode(mode: String) {
        MmkvManager.encodeSettings(AppConfig.PREF_UI_MODE_NIGHT, mode)
        _themeMode.value = mode
    }

    fun setDynamicColorEnabled(enabled: Boolean) {
        MmkvManager.encodeSettings(AppConfig.PREF_DYNAMIC_COLOR, enabled)
        _dynamicColorEnabled.value = enabled
    }

    fun refresh() {
        _themeMode.value =
            MmkvManager.decodeSettingsString(AppConfig.PREF_UI_MODE_NIGHT, "0") ?: "0"
        _dynamicColorEnabled.value =
            MmkvManager.decodeSettingsBool(AppConfig.PREF_DYNAMIC_COLOR, true)
    }
}

@Composable
fun resolveDarkTheme(): Boolean {
    val mode by ThemeManager.themeMode.collectAsState()
    return when (mode) {
        "1" -> false
        else -> true
    }
}

val LocalDarkTheme = compositionLocalOf { false }

@Composable
fun AppTheme(
    darkTheme: Boolean = resolveDarkTheme(),
    content: @Composable () -> Unit
) {
    val mode by ThemeManager.themeMode.collectAsState()
    val colorScheme = when (mode) {
        "1" -> LightColor
        "2" -> HappColor
        else -> DarkColor
    }

    val snackbarController = rememberAppSnackbarController()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context as? Activity ?: return@SideEffect
            val window = activity.window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(
        LocalDarkTheme provides darkTheme,
        LocalAppSnackbar provides snackbarController
    ) {
        MaterialTheme(
            colorScheme = colorScheme
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AppSnackbarBridge(controller = snackbarController)
                content()
                AppSnackbarHost(hostState = snackbarController.hostState)
            }
        }
    }
}
