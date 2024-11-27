
package theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = primaryColor,
    secondary = Green300,
    tertiary = Blue400,

    background = Black,
    surface = DarkGray400,
    surfaceVariant = Gray400,
    onPrimary = Color.White,
    onSecondary = Color.White,
    secondaryContainer = Green200,
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = LightGray400,
    outline = LightGray400,
    tertiaryContainer = Green400
)

private val LightColorScheme = lightColorScheme(
    primary = primaryColor,
    secondary = Green300,
    tertiary = Blue400,
    background = Color(0xFFF7F7F7),
    surface = Color(0xFFF7F7F7),
    surfaceVariant = Gray400,
    onPrimary = Color.White,
    onSecondary = Color.White,
    secondaryContainer = Green200,
    onBackground = Black,
    onSurface = Black,
    onSurfaceVariant = DarkGray400,
    outline = LightGray400,
    inverseOnSurface = Color(0xFFFFFFFF),
    tertiaryContainer = Green400
)

@Composable
internal fun ComposeExperimentalTheme(useDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (!useDarkTheme) {
        LightColorScheme
    } else {
        DarkColorScheme
    }
    MaterialTheme(
        colorScheme = colors,
        shapes = Shapes,
        content = content,
    )
}
