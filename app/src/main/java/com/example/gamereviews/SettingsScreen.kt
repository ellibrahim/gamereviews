package com.example.gamereviews

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController



import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*


import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.text.input.PasswordVisualTransformation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    var isEditingUsername by remember { mutableStateOf(false) }
    var newUsername by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (currentUser == null) {
                // If the user is not logged in, show sign-in and sign-up buttons
                Button(onClick = { navController.navigate("signIn") }) {
                    Text("Sign In")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { navController.navigate("signUp") }) {
                    Text("Sign Up")
                }
            } else {
                // If the user is logged in, show options to change username, password, and sign out
                Text("Signed in as ${currentUser.email}", fontSize = 16.sp)

                Spacer(modifier = Modifier.height(16.dp))

                // Change Username Section
                if (isEditingUsername) {
                    TextField(
                        value = newUsername,
                        onValueChange = { newUsername = it },
                        label = { Text("New Username") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        // Check if the new username is available
                        FirebaseFirestore.getInstance().collection("users")
                            .whereEqualTo("username", newUsername)
                            .get()
                            .addOnSuccessListener { snapshot ->
                                if (snapshot.isEmpty) {
                                    // If the username is available, update it in Firestore
                                    FirebaseFirestore.getInstance().collection("users")
                                        .document(currentUser.uid)
                                        .update("username", newUsername)
                                        .addOnSuccessListener {
                                            successMessage = "Username changed successfully!"
                                            isEditingUsername = false
                                        }
                                        .addOnFailureListener { e ->
                                            errorMessage = "Error updating username: ${e.message}"
                                        }
                                } else {
                                    errorMessage = "Username is already taken."
                                }
                            }
                    }) {
                        Text("Change Username")
                    }
                } else {
                    Button(onClick = { isEditingUsername = true }) {
                        Text("Edit Username")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Change Password Section
                TextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    if (newPassword == confirmPassword) {
                        currentUser.updatePassword(newPassword)
                            .addOnSuccessListener {
                                successMessage = "Password changed successfully!"
                            }
                            .addOnFailureListener { e ->
                                errorMessage = "Error changing password: ${e.message}"
                            }
                    } else {
                        errorMessage = "Passwords do not match."
                    }
                }) {
                    Text("Change Password")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sign out button
                Button(onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("signIn") // Navigate back to sign-in screen
                }) {
                    Text("Sign Out")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display any error or success messages
            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            successMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
