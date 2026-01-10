package np.ict.mad.peyzhixun.ca.feature.game

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun GameScreen(
    onOpenHighScore: () -> Unit
) {

    //Get context to use SharedPreferences later
    val context = LocalContext.current

    // Game states
    var score by remember { mutableStateOf(0) }          //current score
    var highScore by remember { mutableStateOf(loadHighScore(context)) } //saved high score
    var timeLeft by remember { mutableStateOf(30) }      //countdown timer
    var isRunning by remember { mutableStateOf(false) }  //whether game is running
    var moleIndex by remember { mutableStateOf(-1) }     //position of mole (0–8)

    //Timer logic: runs only when game is started
    LaunchedEffect(isRunning) {
        if (isRunning) {
            timeLeft = 30
            score = 0

            //Countdown every second
            while (timeLeft > 0 && isRunning) {
                delay(1000)
                timeLeft--
            }

            //Game ends when timer reaches 0
            isRunning = false
            moleIndex = -1

            //Save high score if current score is higher
            if (score > highScore) {
                highScore = score
                saveHighScore(context, highScore)
            }
        }
    }

    //Mole movement - randomly changes position
    LaunchedEffect(isRunning) {
        while (isRunning) {
            moleIndex = Random.nextInt(9) // random hole from 0 to 8
            delay(700)  // mole moves every 0.7 seconds
        }
    }

    //Main UI layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "MoleRush (Basic)",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            //Display timer, score and high score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Time: $timeLeft")
                Text("Score: $score")
                Text("High: $highScore")
            }

            Spacer(modifier = Modifier.height(16.dp))

            //3x3 game grid using rows
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (row in 0..2) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        for (col in 0..2) {
                            val index = row * 3 + col

                            MoleCell(
                                isMole = (index == moleIndex),
                                enabled = isRunning,
                                onHit = {
                                    // Increase score only if mole is hit
                                    if (index == moleIndex) {
                                        score++
                                        moleIndex = -1 // hide mole after hit
                                    }
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            //Start game button (also works like restart)
            Button(
                onClick = { isRunning = true },
                enabled = !isRunning,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Game")
            }

            Spacer(modifier = Modifier.height(10.dp))

            //Go to High Score screen (navigation proof)
            OutlinedButton(
                onClick = { onOpenHighScore() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("High Score")
            }

            Spacer(modifier = Modifier.height(12.dp))

            //Game over message
            if (!isRunning && timeLeft == 0) {
                Text(
                    text = "Game Over! Your score: $score",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun MoleCell(
    isMole: Boolean,
    enabled: Boolean,
    onHit: () -> Unit
) {
    //Each cell is a button and the mole is shown using a simple text "M"
    Button(
        onClick = onHit,
        enabled = enabled,
        modifier = Modifier.size(90.dp)
    ) {
        Text(if (isMole) "M" else "")
    }
}

//Load saved high score from SharedPreferences
fun loadHighScore(context: Context): Int {
    val prefs = context.getSharedPreferences("mole_rush_basic", Context.MODE_PRIVATE)
    return prefs.getInt("high_score", 0)
}

//Save high score to SharedPreferences
fun saveHighScore(context: Context, score: Int) {
    val prefs = context.getSharedPreferences("mole_rush_basic", Context.MODE_PRIVATE)
    prefs.edit().putInt("high_score", score).apply()
}
