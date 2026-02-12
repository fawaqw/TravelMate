package com.example.travelmate.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.travelmate.ui.navigation.Routes

@Composable
fun LoginScreen(
    navController: NavController,
    vm: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passError by remember { mutableStateOf<String?>(null) }
    var generalError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Sign in to continue your journey",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = if (it.isBlank()) "Email cannot be empty" else null
            },
            label = { Text("Email") },
            isError = emailError != null,
            modifier = Modifier.fillMaxWidth(),
            supportingText = { emailError?.let { Text(it) } }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = pass,
            onValueChange = {
                pass = it
                passError = if (it.length < 6) "Password must be at least 6 characters" else null
            },
            label = { Text("Password") },
            isError = passError != null,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            supportingText = { passError?.let { Text(it) } }
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isBlank() || pass.isBlank()) {
                    generalError = "Please fill in all fields"
                    return@Button
                }
                vm.login(email, pass) { err ->
                    if (err == null) {
                        navController.navigate(Routes.Feed.route) {
                            popUpTo(Routes.Login.route) { inclusive = true }
                        }
                    } else {
                        generalError = err
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Sign In")
        }

        TextButton(
            onClick = { navController.navigate(Routes.SignUp.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Don't have an account? Sign Up")
        }

        generalError?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 16.dp))
        }
    }
}
