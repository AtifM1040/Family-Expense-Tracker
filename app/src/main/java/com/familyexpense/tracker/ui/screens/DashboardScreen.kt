package com.familyexpense.tracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familyexpense.tracker.data.database.ExpenseDatabase
import com.familyexpense.tracker.data.database.ExpenseEntity
import com.familyexpense.tracker.data.repository.ExpenseRepository
import com.familyexpense.tracker.ui.components.GradientButton
import com.familyexpense.tracker.ui.theme.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToAddExpense: () -> Unit
) {
    val context = LocalContext.current
    val database = ExpenseDatabase.getDatabase(context)
    val repository = ExpenseRepository(database.expenseDao(), database.userDao())

    var expenses by remember { mutableStateOf<List<ExpenseEntity>>(emptyList()) }
    var totalExpenses by remember { mutableStateOf(0.0) }
    var monthlyIncome by remember { mutableStateOf(0.0) } // Default value

    LaunchedEffect(Unit) {
        repository.getAllExpenses().collect { expenseList ->
            expenses = expenseList
            totalExpenses = repository.getTotalExpenses()
            if (expenseList.isNotEmpty()) {
                monthlyIncome = expenseList.first().monthlyIncome
            }
        }
    }

    val remainingBalance = monthlyIncome - totalExpenses
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar with Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(GradientGreen, GradientRed)
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = "Dashboard",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Track your family expenses",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // Summary Cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Income Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Monthly Income",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = currencyFormat.format(monthlyIncome),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = GradientGreen
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = GradientGreen,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            // Expenses Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total Expenses",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = currencyFormat.format(totalExpenses),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = GradientRed
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.TrendingDown,
                        contentDescription = null,
                        tint = GradientRed,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            // Remaining Balance Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (remainingBalance >= 0) GradientGreen.copy(alpha = 0.1f)
                    else GradientRed.copy(alpha = 0.1f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Remaining Balance",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = currencyFormat.format(remainingBalance),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (remainingBalance >= 0) GradientGreen else GradientRed
                        )
                    }
                    Icon(
                        imageVector = if (remainingBalance >= 0) Icons.Default.AccountBalance else Icons.Default.Warning,
                        contentDescription = null,
                        tint = if (remainingBalance >= 0) GradientGreen else GradientRed,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Add Expense Button
            GradientButton(
                text = "Add New Monthly Expense",
                onClick = onNavigateToAddExpense,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Expenses List
            Text(
                text = "Recent Expenses",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurfaceLight,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            if (expenses.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No expenses yet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                        Text(
                            text = "Add your first expense to get started",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                    }
                }
            } else {
                expenses.forEach { expense ->
                    ExpenseItem(
                        expense = expense,
                        dateFormat = dateFormat,
                        currencyFormat = currencyFormat
                    )
                }
            }
        }
    }
}

@Composable
fun ExpenseItem(
    expense: ExpenseEntity,
    dateFormat: SimpleDateFormat,
    currencyFormat: NumberFormat
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.description,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = OnSurfaceLight
                )
                Text(
                    text = "${expense.category} • ${dateFormat.format(Date(expense.date))}",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
                Text(
                    text = "Currency: ${expense.currency}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Text(
                text = currencyFormat.format(expense.totalAmount),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = GradientRed
            )
        }
    }
}