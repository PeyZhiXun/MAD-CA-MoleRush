package np.ict.mad.peyzhixun.ca.feature.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun HighScoreScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val highScore = loadHighScore(context)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text("High Score", style = MaterialTheme.typography.headlineSmall)

            Spacer(Modifier.height(16.dp))

            Text(
                text = highScore.toString(),
                style = MaterialTheme.typography.displayMedium
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}

