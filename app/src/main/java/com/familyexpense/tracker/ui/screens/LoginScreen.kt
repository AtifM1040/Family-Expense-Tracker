package com.familyexpense.tracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familyexpense.tracker.data.database.ExpenseDatabase
import com.familyexpense.tracker.data.repository.ExpenseRepository
import com.familyexpense.tracker.ui.components.GradientButton
import com.familyexpense.tracker.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToSignup: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val database = ExpenseDatabase.getDatabase(context)
    val repository = ExpenseRepository(database.expenseDao(), database.userDao())
    val scope = rememberCoroutineScope()

    val isFormValid = email.isNotBlank() &&
            password.isNotBlank() &&
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BackgroundLight, Color.White),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            Text(
                text = "Welcome Back",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = GradientGreen
            )

            Text(
                text = "Sign in to continue tracking expenses",
                fontSize = 16.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GradientGreen,
                    focusedLabelColor = GradientGreen
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GradientGreen,
                    focusedLabelColor = GradientGreen
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Forgot Password
            TextButton(
                onClick = {
                    snackbarMessage = "Password reset feature coming soon!"
                    showSnackbar = true
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "Forgot Password?",
                    color = GradientGreen
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            GradientButton(
                text = "Sign In",
                onClick = {
                    scope.launch {
                        try {
                            val user = repository.loginUser(email, password)
                            if (user != null) {
                                onNavigateToDashboard()
                            } else {
                                snackbarMessage = "Invalid email or password"
                                showSnackbar = true
                            }
                        } catch (e: Exception) {
                            snackbarMessage = "Login failed. Please try again."
                            showSnackbar = true
                        }
                    }
                },
                enabled = isFormValid
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Text(
                    text = "Don't have an account? ",
                    color = TextSecondary
                )
                TextButton(onClick = onNavigateToSignup) {
                    Text(
                        text = "Sign Up",
                        color = GradientGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (showSnackbar) {
            LaunchedEffect(showSnackbar) {
                kotlinx.coroutines.delay(3000)
                showSnackbar = false
            }

            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                action = {
                    TextButton(onClick = { showSnackbar = false }) {
                        Text("Dismiss", color = Color.White)
                    }
                }
            ) {
                Text(snackbarMessage)
            }
        }
    }
}