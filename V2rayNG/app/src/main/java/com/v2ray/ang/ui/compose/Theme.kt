package com.v2ray.ang.ui.compose

import android.app.Activity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.v2ray.ang.AppConfig
import com.v2ray.ang.R
import com.v2ray.ang.handler.MmkvManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Mode 1: light with red accent
private val LightColor = lightColorScheme(
    primary = Color(0xFFD92B3F),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFDADD),
    onPrimaryContainer = Color(0xFF410008),
    secondary = Color(0xFF75565A),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF3DDDF),
    onSecondaryContainer = Color(0xFF2B1518),
    tertiary = Color(0xFF059669),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFD1FAE5),
    onTertiaryContainer = Color(0xFF022C22),
    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFFDAD6),
    onError = Color(0xFFFFFFFF),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFF8F8),
    onBackground = Color(0xFF231819),
    surface = Color(0xFFFFF8F8),
    onSurface = Color(0xFF231819),
    surfaceVariant = Color(0xFFF3DDDF),
    onSurfaceVariant = Color(0xFF524345),
    outline = Color(0xFF857375),
    outlineVariant = Color(0xFFD8C2C4),
    inverseSurface = Color(0xFF382D2E),
    inverseOnSurface = Color(0xFFFFEDED),
    inversePrimary = Color(0xFFFFB3B6),
    scrim = Color(0xFF000000),
    surfaceTint = Color(0xFFD92B3F),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFFFF0F0),
    surfaceContainer = Color(0xFFFBE9EA),
    surfaceContainerHigh = Color(0xFFF5E3E4),
    surfaceContainerHighest = Color(0xFFEFDDDE),
)

// Mode 0: dark red
private val DarkColor = darkColorScheme(
    primary = Color(0xFFFF3B4E),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF3A1219),
    onPrimaryContainer = Color(0xFFFFE3E6),
    secondary = Color(0xFFFF7A87),
    onSecondary = Color(0xFF3A0A10),
    secondaryContainer = Color(0xFF4A1A21),
    onSecondaryContainer = Color(0xFFFFD9DD),
    tertiary = Color(0xFFFFB454),
    onTertiary = Color(0xFF3A2500),
    tertiaryContainer = Color(0xFF4A3210),
    onTertiaryContainer = Color(0xFFFFE2B8),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color.Transparent,
    onBackground = Color(0xFFFFE3E6),
    surface = Color(0xFF140A0C),
    onSurface = Color(0xFFFFE3E6),
    surfaceVariant = Color(0xFF2A1519),
    onSurfaceVariant = Color(0xFFC79BA2),
    outline = Color(0xFF9A6A72),
    outlineVariant = Color(0xFF3A1A20),
    inverseSurface = Color(0xFFFFE3E6),
    inverseOnSurface = Color(0xFF140A0C),
    inversePrimary = Color(0xFFB3202F),
    scrim = Color(0xFF000000),
    surfaceTint = Color(0xFFFF3B4E),
    surfaceContainerLowest = Color(0xFF0F0708),
    surfaceContainerLow = Color(0xFF190D10),
    surfaceContainer = Color(0xFF1E1013),
    surfaceContainerHigh = Color(0xFF2A1519),
    surfaceContainerHighest = Color(0xFF361C21),
)

// Mode 2: dark wine / rose
private val HappColor = darkColorScheme(
    primary = Color(0xFFFF5C7A),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF43182B),
    onPrimaryContainer = Color(0xFFFFDDE6),
    secondary = Color(0xFFFFA3B5),
    onSecondary = Color(0xFF3F1020),
    secondaryContainer = Color(0xFF3A1E29),
    onSecondaryContainer = Color(0xFFFFDDE6),
    tertiary = Color(0xFF7CE0C3),
    onTertiary = Color(0xFF00382E),
    tertiaryContainer = Color(0xFF005143),
    onTertiaryContainer = Color(0xFFA0F2D0),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color.Transparent,
    onBackground = Color(0xFFFFE8EE),
    surface = Color(0xFF150B10),
    onSurface = Color(0xFFFFE8EE),
    surfaceVariant = Color(0xFF2C1722),
    onSurfaceVariant = Color(0xFFD1A9B6),
    outline = Color(0xFF9C7482),
    outlineVariant = Color(0xFF37202C),
    inverseSurface = Color(0xFFFFE8EE),
    inverseOnSurface = Color(0xFF150B10),
    inversePrimary = Color(0xFFB02A4A),
    scrim = Color(0xFF000000),
    surfaceTint = Color(0xFFFF5C7A),
    surfaceContainerLowest = Color(0xFF0F070B),
    surfaceContainerLow = Color(0xFF1A0E14),
    surfaceContainer = Color(0xFF211018),
    surfaceContainerHigh = Color(0xFF2C1722),
    surfaceContainerHighest = Color(0xFF37202C),
)

private fun neonScheme(
    accent: Color,
    onAccent: Color,
    accentContainer: Color,
    onAccentContainer: Color,
    secondary: Color,
    text: Color,
    textVariant: Color,
    outline: Color,
    outlineVariant: Color,
    base: Color,
    low: Color,
    mid: Color,
    high: Color,
    highest: Color,
) = darkColorScheme(
    primary = accent,
    onPrimary = onAccent,
    primaryContainer = accentContainer,
    onPrimaryContainer = onAccentContainer,
    secondary = secondary,
    onSecondary = onAccent,
    secondaryContainer = accentContainer,
    onSecondaryContainer = onAccentContainer,
    tertiary = secondary,
    onTertiary = onAccent,
    tertiaryContainer = accentContainer,
    onTertiaryContainer = onAccentContainer,
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color.Transparent,
    onBackground = text,
    surface = base,
    onSurface = text,
    surfaceVariant = high,
    onSurfaceVariant = textVariant,
    outline = outline,
    outlineVariant = outlineVariant,
    inverseSurface = text,
    inverseOnSurface = base,
    inversePrimary = accentContainer,
    scrim = Color(0xFF000000),
    surfaceTint = accent,
    surfaceContainerLowest = base,
    surfaceContainerLow = low,
    surfaceContainer = mid,
    surfaceContainerHigh = high,
    surfaceContainerHighest = highest,
)

// Mode 3: dark with white shimmering outline
private val WhiteColor = neonScheme(
    accent = Color(0xFFF5F5F7),
    onAccent = Color(0xFF111114),
    accentContainer = Color(0xFF2A2A30),
    onAccentContainer = Color(0xFFFFFFFF),
    secondary = Color(0xFFC7C7CC),
    text = Color(0xFFF5F5F7),
    textVariant = Color(0xFFA1A1A8),
    outline = Color(0xFF6E6E75),
    outlineVariant = Color(0xFF2C2C31),
    base = Color(0xFF0B0B0D),
    low = Color(0xFF101012),
    mid = Color(0xFF151518),
    high = Color(0xFF1D1D21),
    highest = Color(0xFF26262B),
)

// Mode 4: dark neon green
private val GreenColor = neonScheme(
    accent = Color(0xFF2BFF88),
    onAccent = Color(0xFF003919),
    accentContainer = Color(0xFF0B3D26),
    onAccentContainer = Color(0xFFC2FFDD),
    secondary = Color(0xFF7CFFB8),
    text = Color(0xFFE3FFEF),
    textVariant = Color(0xFF8FC4A5),
    outline = Color(0xFF5A8F72),
    outlineVariant = Color(0xFF1E3A2B),
    base = Color(0xFF06100B),
    low = Color(0xFF0A1710),
    mid = Color(0xFF0C1A13),
    high = Color(0xFF12261C),
    highest = Color(0xFF183226),
)

// Mode 5: dark neon blue
private val BlueColor = neonScheme(
    accent = Color(0xFF00B4FF),
    onAccent = Color(0xFF00212E),
    accentContainer = Color(0xFF0B3A57),
    onAccentContainer = Color(0xFFD6F1FF),
    secondary = Color(0xFF00E5FF),
    text = Color(0xFFD6F1FF),
    textVariant = Color(0xFF8FB0D0),
    outline = Color(0xFF5F89B0),
    outlineVariant = Color(0xFF1B3048),
    base = Color(0xFF070C18),
    low = Color(0xFF09111F),
    mid = Color(0xFF0B1526),
    high = Color(0xFF111E33),
    highest = Color(0xFF18283F),
)

// Semantic Colors
val colorPing = Color(0xFF3DDC97) // Green
val colorPingRed = Color(0xFFFF6B7A) // Red
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
private fun ShimmerFrame(content: @Composable () -> Unit) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing)
        ),
        label = "shimmerAngle"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0B0D))
            .drawBehind {
                val c = center
                val side = maxOf(size.width, size.height) * 1.6f
                rotate(degrees = angle, pivot = c) {
                    drawRect(
                        brush = Brush.sweepGradient(
                            0f to Color(0xFF15151A),
                            0.2f to Color(0xFFFFFFFF),
                            0.4f to Color(0xFF15151A),
                            0.7f to Color(0xFFB8B8C0),
                            1f to Color(0xFF15151A),
                            center = c
                        ),
                        topLeft = Offset(c.x - side / 2f, c.y - side / 2f),
                        size = Size(side, side)
                    )
                }
            }
            .padding(2.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0xFF0B0B0D))
    ) {
        content()
    }
}

@Composable
fun AppTheme(
    darkTheme: Boolean = resolveDarkTheme(),
    content: @Composable () -> Unit
) {
    val mode by ThemeManager.themeMode.collectAsState()
    val colorScheme = when (mode) {
        "1" -> LightColor
        "2" -> HappColor
        "3" -> WhiteColor
        "4" -> GreenColor
        "5" -> BlueColor
        else -> DarkColor
    }
    val gradient: List<Color>? = when (mode) {
        "1" -> null
        "2" -> listOf(Color(0xFF150B10), Color(0xFF2E1020))
        "3" -> listOf(Color(0xFF0B0B0D), Color(0xFF18181D))
        "4" -> listOf(Color(0xFF06100B), Color(0xFF0A2417))
        "5" -> listOf(Color(0xFF070C18), Color(0xFF0A1F3D))
        else -> listOf(Color(0xFF140A0C), Color(0xFF2A0F14))
    }
    val showVideo = mode != "1"
    val tintVideo = mode != "1" && mode != "3"

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
            val accent = MaterialTheme.colorScheme.primary
            val body: @Composable () -> Unit = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            if (gradient != null) Modifier.background(Brush.verticalGradient(gradient))
                            else Modifier
                        )
                ) {
                    if (showVideo) {
                        VideoBackdrop(R.raw.bg_white)
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.30f))
                        )
                        if (tintVideo) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(accent.copy(alpha = 0.28f))
                            )
                        }
                    }
                    AppSnackbarBridge(controller = snackbarController)
                    content()
                    AppSnackbarHost(hostState = snackbarController.hostState)
                }
            }
            if (mode == "3") ShimmerFrame(body) else body()
        }
    }
}
