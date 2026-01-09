package np.ict.mad.peyzhixun.ca.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import np.ict.mad.peyzhixun.ca.feature.auth.LoginScreen
import np.ict.mad.peyzhixun.ca.feature.auth.RegisterScreen
import np.ict.mad.peyzhixun.ca.feature.game.GameScreen

@Composable
fun AppNav(
    navController: NavHostController,
    startDestination: String
) {
    var currentDisplayName = "" // simple way for CA (no advanced state mgmt)

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { user ->
                    currentDisplayName = user.displayName
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
                onRegisterSuccess = {
                    // after successful register, go back to login
                    navController.popBackStack()
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.GAME) {
            GameScreen(
                displayName = currentDisplayName,
                navController = navController
            )
        }
    }
}

