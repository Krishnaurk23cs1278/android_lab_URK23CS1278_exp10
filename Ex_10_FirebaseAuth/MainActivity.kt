package com.example.firebaseauthapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LoginScreen()
            }
        }
    }
}

@Composable
fun LoginScreen() {
    val auth = remember { Firebase.auth }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var signedIn by remember { mutableStateOf(auth.currentUser != null) }
    var message by remember { mutableStateOf("") }

    if (signedIn) {
        Column(Modifier.padding(24.dp)) {
            Text("Welcome! You are signed in.")
            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                auth.signOut()
                signedIn = false
                message = "Signed out"
            }) {
                Text("Sign Out")
            }
        }
    } else {
        Column(Modifier.padding(24.dp)) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    auth.signInWithEmailAndPassword(email.trim(), password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                signedIn = true
                                message = "Login successful"
                            } else {
                                message = "Login failed. Check credentials."
                            }
                        }
                }) { Text("Login") }

                Button(onClick = {
                    auth.createUserWithEmailAndPassword(email.trim(), password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                signedIn = true
                                message = "Account created"
                            } else {
                                message = "Registration failed"
                            }
                        }
                }) { Text("Register") }
            }
            if (message.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(message)
            }
        }
    }
}
