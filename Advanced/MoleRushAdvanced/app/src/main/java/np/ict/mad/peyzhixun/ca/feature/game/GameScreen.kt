package np.ict.mad.peyzhixun.ca.feature.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import np.ict.mad.peyzhixun.ca.R
import np.ict.mad.peyzhixun.ca.data.AppDatabase
import np.ict.mad.peyzhixun.ca.data.LoginPreferences
import np.ict.mad.peyzhixun.ca.data.ScoreEntity
import np.ict.mad.peyzhixun.ca.navigation.Routes
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    userId: Int,
    displayName: String,
    navController: NavHostController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val db = remember { AppDatabase.getInstance(context) }
    val scoreDb = remember { db.scoreDb() }
    val prefs = remember { LoginPreferences(context) }

    //Game states
    var score by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(30) }
    var isRunning by remember { mutableStateOf(false) }
    var moleIndex by remember { mutableStateOf(-1) }
    var showGameOver by remember { mutableStateOf(false) }

    //Advanced: show best scores from Room
    var yourBest by remember { mutableStateOf(0) }
    var globalBest by remember { mutableStateOf(0) }

    fun refreshBests() {
        scope.launch {
            globalBest = scoreDb.getGlobalBest() ?: 0
            yourBest = if (userId != -1) (scoreDb.getBestForUser(userId) ?: 0) else 0
        }
    }

    LaunchedEffect(userId) {
        if (userId != -1) refreshBests()
    }

    //Timer logic
    LaunchedEffect(isRunning) {
        if (isRunning) {
            showGameOver = false
            timeLeft = 30
            score = 0

            while (timeLeft > 0 && isRunning) {
                delay(1000)
                timeLeft--
            }

            //Game ends
            if (timeLeft == 0) {
                isRunning = false
                moleIndex = -1
                showGameOver = true

                //Save score into Room (advanced requirement)
                scope.launch {
                    if (userId != -1) {
                        scoreDb.addScore(
                            ScoreEntity(
                                userId = userId,
                                score = score,
                                timestamp = System.currentTimeMillis()
                            )
                        )
                        refreshBests()
                    }
                }
            }
        }
    }

    //Mole movement
    LaunchedEffect(isRunning) {
        while (isRunning) {
            moleIndex = Random.nextInt(9)
            delay(800) //0.8s is nice and playable
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    //Logo beside MoleRush title
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.molelogo),
                            contentDescription = "MoleRush Logo",
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("MoleRush")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.HIGH_SCORE) }) {
                        Icon(Icons.Filled.EmojiEvents, contentDescription = "High Scores")
                    }
                    IconButton(
                        onClick = {
                            prefs.clear()
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.GAME) { inclusive = true }
                            }
                        }
                    ) {
                        Icon(Icons.Filled.Logout, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = if (displayName.isNotBlank()) "Welcome, $displayName" else "Welcome",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(12.dp))

            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Time: $timeLeft", style = MaterialTheme.typography.titleMedium)
                        Text("Score: $score", style = MaterialTheme.typography.titleMedium)
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Your Best: $yourBest")
                        Text("Global Best: $globalBest")
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            //3x3 grid using Rows
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                for (row in 0..2) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        for (col in 0..2) {
                            val index = row * 3 + col
                            val isMole = index == moleIndex

                            ElevatedButton(
                                onClick = {
                                    if (isRunning && isMole) {
                                        score++
                                        moleIndex = -1
                                    }
                                },
                                enabled = isRunning,
                                modifier = Modifier.size(95.dp),
                                contentPadding = PaddingValues(0.dp) //
                            ) {
                                // mole image
                                if (isMole) {
                                    Image(
                                        painter = painterResource(id = R.drawable.molee),
                                        contentDescription = "Mole",
                                        modifier = Modifier.size(60.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { isRunning = true },
                enabled = !isRunning,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (showGameOver) "Restart" else "Start Game")
            }

            if (showGameOver) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Game Over! Final Score: $score",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
