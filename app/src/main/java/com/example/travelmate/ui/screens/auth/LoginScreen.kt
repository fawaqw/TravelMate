package com.example.travelmate.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(Modifier.padding(16.dp)) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(email, { email = it }, label = { Text("Email") })
        OutlinedTextField(pass, { pass = it }, label = { Text("Password") })

        Button(
            onClick = {
                vm.login(email, pass) { error ->
                    if (error == null) {
                        navController.navigate(Routes.Feed.route) {
                            popUpTo(Routes.Login.route) { inclusive = true }
                        }
                    } else {
                        errorMessage = error
                    }
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Login")
        }

        TextButton(onClick = { navController.navigate(Routes.SignUp.route) }) {
            Text("Don't have an account? Sign Up")
        }

        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}
