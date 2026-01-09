package np.ict.mad.peyzhixun.ca.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val GameDarkColors = darkColorScheme(
    background = GameBg,
    surface = GameSurface,

    primary = NeonCyan,
    secondary = NeonPink,
    tertiary = AcidYellow,

    onBackground = TextOnDark,
    onSurface = TextOnDark,

    error = DangerRed,
    onError = GameBg
)

@Composable
fun ContinuousAssessmentPeyZhiXunTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GameDarkColors,
        typography = Typography,
        content = content
    )
}
