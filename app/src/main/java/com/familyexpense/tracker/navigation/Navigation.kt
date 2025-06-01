package com.familyexpense.tracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.familyexpense.tracker.ui.screens.AddExpenseScreen
import com.familyexpense.tracker.ui.screens.DashboardScreen
import com.familyexpense.tracker.ui.screens.LoginScreen
import com.familyexpense.tracker.ui.screens.LogoScreen
import com.familyexpense.tracker.ui.screens.SignupScreen

@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "logo",
        modifier = modifier
    ) {
        composable("logo") {
            LogoScreen(
                onNavigateToLogin = {
                    navController.navigate("login")
                }
            )
        }

        composable("login") {
            LoginScreen(
                onNavigateToSignup = {
                    navController.navigate("signup")
                },
                onNavigateToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("logo") { inclusive = true }
                    }
                }
            )
        }

        composable("signup") {
            SignupScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("logo") { inclusive = true }
                    }
                }
            )
        }

        composable("dashboard") {
            DashboardScreen(
                onNavigateToAddExpense = {
                    navController.navigate("add_expense")
                }
            )
        }

        composable("add_expense") {
            AddExpenseScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}