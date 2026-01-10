package np.ict.mad.peyzhixun.ca.feature.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import np.ict.mad.peyzhixun.ca.R
import np.ict.mad.peyzhixun.ca.data.*
import np.ict.mad.peyzhixun.ca.ui.theme.GameBg
import np.ict.mad.peyzhixun.ca.ui.theme.GameSurface2

@Composable
fun LoginScreen(
    onLoginSuccess: (UserEntity) -> Unit,
    onNavigateRegister: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val db = remember { AppDatabase.getInstance(context) }
    val manager = remember { LoginManager(db.userDb()) }
    val prefs = remember { LoginPreferences(context) }

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var rememberMe by rememberSaveable { mutableStateOf(false) }
    var showPassword by rememberSaveable { mutableStateOf(false) }
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
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // LOGO
                Image(
                    painter = painterResource(id = R.drawable.molelogo),
                    contentDescription = "MoleRush Logo",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "MoleRush",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Tap fast. Score big.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )

                Spacer(Modifier.height(24.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; error = null },
                    label = { Text("Email Address") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    )
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

                Spacer(Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
                    Text("Remember Me")
                }

                if (error != null) {
                    Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            error = "Please fill in all fields."
                            return@Button
                        }

                        scope.launch {
                            loading = true
                            val user = manager.loginUser(email, password)
                            loading = false

                            if (user == null) {
                                error = "Invalid email or password."
                            } else {
                                if (rememberMe) {
                                    prefs.saveLogin(user.id, user.email, user.displayName)
                                } else {
                                    prefs.clear()
                                }
                                onLoginSuccess(user)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    enabled = !loading
                ) {
                    Text(if (loading) "Signing In..." else "Sign In")
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    "Don't have an account? Register here",
                    modifier = Modifier.clickable { onNavigateRegister() },
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
