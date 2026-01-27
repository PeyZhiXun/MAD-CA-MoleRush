package np.ict.mad.peyzhixun.ca.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import np.ict.mad.peyzhixun.ca.feature.game.GameScreen
import np.ict.mad.peyzhixun.ca.feature.game.HighScoreScreen

object Routes {
    const val GAME = "game"
    const val HIGH_SCORE = "high_score"
}

@Composable
fun AppNav(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.GAME
    ) {
        composable(Routes.GAME) {
            GameScreen(
                onOpenHighScore = { navController.navigate(Routes.HIGH_SCORE) }
            )
        }

        composable(Routes.HIGH_SCORE) {
            HighScoreScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
