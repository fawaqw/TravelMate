package com.example.travelmate.ui.screens.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    fun isLoggedIn() = auth.currentUser != null

    fun login(email: String, pass: String, onResult: (String?) -> Unit) {
        if (email.isBlank() || pass.isBlank()) {
            onResult("Email and password cannot be empty")
            return
        }
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(null)
                } else {
                    onResult(task.exception?.localizedMessage ?: "Login failed")
                }
            }
    }

    fun register(name: String, email: String, pass: String, onResult: (String?) -> Unit) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            onResult("All fields are required")
            return
        }
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                        onResult(null)
                    }
                } else {
                    onResult(task.exception?.localizedMessage ?: "Registration failed")
                }
            }
    }

    fun logout() = auth.signOut()
}
