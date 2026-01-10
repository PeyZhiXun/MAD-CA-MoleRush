package np.ict.mad.peyzhixun.ca.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import np.ict.mad.peyzhixun.ca.data.LoginPreferences
import np.ict.mad.peyzhixun.ca.feature.auth.LoginScreen
import np.ict.mad.peyzhixun.ca.feature.auth.RegisterScreen
import np.ict.mad.peyzhixun.ca.feature.game.GameScreen
import np.ict.mad.peyzhixun.ca.feature.game.HighScoreScreen

@Composable
fun AppNav(
    navController: NavHostController,
    startDestination: String
) {
    val context = LocalContext.current
    val prefs = remember { LoginPreferences(context) }

    //Compose state
    var currentDisplayName by rememberSaveable { mutableStateOf("") }
    var currentUserId by rememberSaveable { mutableIntStateOf(-1) }

    LaunchedEffect(startDestination) {
        if (startDestination == Routes.GAME && prefs.isRemembered()) {
            currentUserId = prefs.getUserId()
            currentDisplayName = prefs.getDisplayName()
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { user ->
                    currentDisplayName = user.displayName
                    currentUserId = user.id

                    navController.navigate(Routes.GAME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        composable(Routes.GAME) {
            GameScreen(
                userId = currentUserId,
                displayName = currentDisplayName,
                navController = navController
            )
        }

        composable(Routes.HIGH_SCORE) {
            HighScoreScreen(
                userId = currentUserId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
