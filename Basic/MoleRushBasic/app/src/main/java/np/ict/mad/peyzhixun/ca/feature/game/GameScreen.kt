package np.ict.mad.peyzhixun.ca.feature.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import np.ict.mad.peyzhixun.ca.data.LoginPreferences
import np.ict.mad.peyzhixun.ca.navigation.Routes

@Composable
fun GameScreen(
    displayName: String,
    navController: NavHostController
) {
    val context = LocalContext.current
    val prefs = LoginPreferences(context)

    Box(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = if (displayName.isNotBlank()) "Welcome, $displayName!" else "Welcome!",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(12.dp))

            Text("Game Screen (Wack-a-Mole will be here)")

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    // 1) Clear saved login
                    prefs.clear()

                    // 2) Go back to LOGIN and clear backstack
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.GAME) { inclusive = true }
                    }
                }
            ) {
                Text("Logout")
            }
        }
    }
}
