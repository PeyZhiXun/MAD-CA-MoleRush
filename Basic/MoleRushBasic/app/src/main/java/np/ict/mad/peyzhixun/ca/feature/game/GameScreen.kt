package np.ict.mad.peyzhixun.ca.feature.game

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    onOpenHighScore: () -> Unit
) {
    val context = LocalContext.current

    var score by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(30) }
    var moleIndex by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }

    var highScore by remember { mutableStateOf(loadHighScore(context)) }

    //Start/Restart
    fun startOrRestart() {
        score = 0
        timeLeft = 30
        moleIndex = Random.nextInt(9)
        isRunning = true
    }

    //Timer
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }

            isRunning = false

            //Update high score on game end
            if (score > highScore) {
                highScore = score
                saveHighScore(context, highScore)
            }
        }
    }

    //Mole movement
    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(Random.nextLong(700, 1001))
            moleIndex = Random.nextInt(9)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text("Wack-a-Mole", fontWeight = FontWeight.SemiBold) },
            actions = {
                IconButton(onClick = onOpenHighScore) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "High Scores"
                    )
                }
            }
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Score: $score", fontWeight = FontWeight.Medium)
            Text("Time: $timeLeft", fontWeight = FontWeight.Medium)
        }

        Spacer(Modifier.height(6.dp))
        Text("High Score: $highScore")

        Spacer(Modifier.height(18.dp))

        //3x3 grid
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (row in 0..2) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    for (col in 0..2) {
                        val index = row * 3 + col

                        HoleButton(
                            isMole = isRunning && index == moleIndex,
                            enabled = isRunning,
                            onClick = {
                                if (isRunning && index == moleIndex) {
                                    score++
                                }
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { startOrRestart() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isRunning) "Restart" else "Start")
        }

        Spacer(Modifier.height(12.dp))

        //Game over
        if (!isRunning && timeLeft == 0) {
            Text(
                text = "Game Over! Final score: $score",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun HoleButton(
    isMole: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.size(90.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = if (isMole) "M" else "",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

//SharedPreferences
fun loadHighScore(context: Context): Int {
    val prefs = context.getSharedPreferences("mole_rush_basic", Context.MODE_PRIVATE)
    return prefs.getInt("high_score", 0)
}

fun saveHighScore(context: Context, score: Int) {
    val prefs = context.getSharedPreferences("mole_rush_basic", Context.MODE_PRIVATE)
    prefs.edit().putInt("high_score", score).apply()
}
