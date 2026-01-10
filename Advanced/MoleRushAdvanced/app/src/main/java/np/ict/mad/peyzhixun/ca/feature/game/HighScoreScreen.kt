package np.ict.mad.peyzhixun.ca.feature.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import np.ict.mad.peyzhixun.ca.data.AppDatabase
import np.ict.mad.peyzhixun.ca.data.ScoreRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighScoreScreen(
    userId: Int,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val db = remember { AppDatabase.getInstance(context) }
    val scoreDb = remember { db.scoreDb() }

    var yourBest by remember { mutableStateOf(0) }
    var top5 by remember { mutableStateOf<List<ScoreRow>>(emptyList()) }

    LaunchedEffect(userId) {
        scope.launch {
            yourBest = scoreDb.getBestForUser(userId) ?: 0
            top5 = scoreDb.getTop5BestScores()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("High Scores") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Your Best", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = yourBest.toString(),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Text("Top 5 Players", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    if (top5.isEmpty()) {
                        Text("No scores yet. Play a game first!")
                    } else {
                        top5.forEachIndexed { index, row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${index + 1}. ${row.displayName}")
                                Text(row.bestScore.toString())
                            }
                            if (index != top5.lastIndex) Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }
    }
}
