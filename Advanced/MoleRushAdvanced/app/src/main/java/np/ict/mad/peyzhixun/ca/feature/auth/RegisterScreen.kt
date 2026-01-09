package np.ict.mad.peyzhixun.ca.feature.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import np.ict.mad.peyzhixun.ca.R
import np.ict.mad.peyzhixun.ca.data.AppDatabase
import np.ict.mad.peyzhixun.ca.data.LoginManager
import np.ict.mad.peyzhixun.ca.ui.theme.GameBg
import np.ict.mad.peyzhixun.ca.ui.theme.GameSurface2

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val db = remember { AppDatabase.getInstance(context) }
    val manager = remember { LoginManager(db.userDb()) }

    var displayName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    var showPassword by rememberSaveable { mutableStateOf(false) }
    var showConfirmPassword by rememberSaveable { mutableStateOf(false) }

    var error by rememberSaveable { mutableStateOf<String?>(null) }
    var loading by rememberSaveable { mutableStateOf(false) }

    val gradient = Brush.verticalGradient(listOf(GameBg, GameSurface2))

    Box(
        modifier = Modifier.fillMaxSize().background(gradient).padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // LOGO
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "MoleRush Logo",
                    modifier = Modifier.size(140.dp)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "MoleRush",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = displayName,
                    onValueChange = { displayName = it; error = null },
                    label = { Text("Display Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; error = null },
                    label = { Text("Email Address") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; error = null },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation =
                        if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { showPassword = !showPassword }) {
                            Text(if (showPassword) "Hide" else "Show")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; error = null },
                    label = { Text("Confirm Password") },
                    singleLine = true,
                    visualTransformation =
                        if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                            Text(if (showConfirmPassword) "Hide" else "Show")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                if (error != null) {
                    Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                    )
                }

                Spacer(Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (displayName.isBlank() || email.isBlank() ||
                            password.isBlank() || confirmPassword.isBlank()
                        ) {
                            error = "Please fill in all fields."
                            return@Button
                        }
                        if (password != confirmPassword) {
                            error = "Passwords do not match."
                            return@Button
                        }

                        scope.launch {
                            loading = true
                            val result = manager.registerUser(email, password, displayName)
                            loading = false

                            if (result != null) {
                                error = result
                            } else {
                                onRegisterSuccess()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    enabled = !loading
                ) {
                    Text(if (loading) "Creating..." else "Create Account")
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    "Already have an account? Back to Login",
                    modifier = Modifier.clickable { onBackToLogin() },
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
