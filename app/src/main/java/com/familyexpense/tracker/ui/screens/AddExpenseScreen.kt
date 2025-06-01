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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.familyexpense.tracker.data.database.ExpenseDatabase
import com.familyexpense.tracker.data.database.ExpenseEntity
import com.familyexpense.tracker.data.repository.ExpenseRepository
import com.familyexpense.tracker.ui.components.GradientButton
import com.familyexpense.tracker.ui.theme.*
import kotlinx.coroutines.launch
import java.util.*
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    onNavigateBack: () -> Unit
) {
    var monthlyIncome by remember { mutableStateOf("") }
    var selectedCurrency by remember { mutableStateOf("USD") }
    var selectedMonth by remember { mutableStateOf("January") }
    var daysInMonth by remember { mutableStateOf(31) }
    var totalAmount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Food") }
    var showCurrencyDropdown by remember { mutableStateOf(false) }
    var showMonthDropdown by remember { mutableStateOf(false) }
    var showCategoryDropdown by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val database = ExpenseDatabase.getDatabase(context)
    val repository = ExpenseRepository(database.expenseDao(), database.userDao())
    val scope = rememberCoroutineScope()

    val currencies = listOf("USD", "EUR", "GBP", "JPY", "CAD", "AUD", "INR", "PKR")
    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    val categories = listOf(
        "Food", "Transportation", "Entertainment", "Healthcare",
        "Shopping", "Bills", "Education", "Other"
    )

    // Update days in month based on selected month
    LaunchedEffect(selectedMonth) {
        daysInMonth = when (selectedMonth) {
            "February" -> 28 // Simplified, not accounting for leap years
            "April", "June", "September", "November" -> 30
            else -> 31
        }
    }

    val isFormValid = monthlyIncome.isNotBlank() &&
            totalAmount.isNotBlank() &&
            description.isNotBlank() &&
            monthlyIncome.toDoubleOrNull() != null &&
            totalAmount.toDoubleOrNull() != null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(GradientGreen, GradientRed)
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add New Expense",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Form Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Monthly Income Field
                OutlinedTextField(
                    value = monthlyIncome,
                    onValueChange = { monthlyIncome = it },
                    label = { Text("Monthly Income") },
                    leadingIcon = {
                        Icon(Icons.Default.AttachMoney, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GradientGreen,
                        focusedLabelColor = GradientGreen
                    )
                )

                // Currency Dropdown
                ExposedDropdownMenuBox(
                    expanded = showCurrencyDropdown,
                    onExpandedChange = { showCurrencyDropdown = !showCurrencyDropdown },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    OutlinedTextField(
                        value = selectedCurrency,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Currency") },
                        leadingIcon = {
                            Icon(Icons.Default.AttachMoney, contentDescription = null)
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCurrencyDropdown)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GradientGreen,
                            focusedLabelColor = GradientGreen
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = showCurrencyDropdown,
                        onDismissRequest = { showCurrencyDropdown = false }
                    ) {
                        currencies.forEach { currency ->
                            DropdownMenuItem(
                                text = { Text(currency) },
                                onClick = {
                                    selectedCurrency = currency
                                    showCurrencyDropdown = false
                                }
                            )
                        }
                    }
                }

                // Month Dropdown
                ExposedDropdownMenuBox(
                    expanded = showMonthDropdown,
                    onExpandedChange = { showMonthDropdown = !showMonthDropdown },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    OutlinedTextField(
                        value = selectedMonth,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Month") },
                        leadingIcon = {
                            Icon(Icons.Default.CalendarMonth, contentDescription = null)
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = showMonthDropdown)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GradientGreen,
                            focusedLabelColor = GradientGreen
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = showMonthDropdown,
                        onDismissRequest = { showMonthDropdown = false }
                    ) {
                        months.forEach { month ->
                            DropdownMenuItem(
                                text = { Text(month) },
                                onClick = {
                                    selectedMonth = month
                                    showMonthDropdown = false
                                }
                            )
                        }
                    }
                }

                // Days in Month Display
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.7f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            tint = GradientGreen
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Days in $selectedMonth: $daysInMonth",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = showCategoryDropdown,
                    onExpandedChange = { showCategoryDropdown = !showCategoryDropdown },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Category") },
                        leadingIcon = {
                            Icon(Icons.Default.Category, contentDescription = null)
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryDropdown)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GradientGreen,
                            focusedLabelColor = GradientGreen
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = showCategoryDropdown,
                        onDismissRequest = { showCategoryDropdown = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    showCategoryDropdown = false
                                }
                            )
                        }
                    }
                }

                // Total Amount Field
                OutlinedTextField(
                    value = totalAmount,
                    onValueChange = { totalAmount = it },
                    label = { Text("Total Amount") },
                    leadingIcon = {
                        Text(
                            text = selectedCurrency,
                            modifier = Modifier.padding(start = 12.dp),
                            fontWeight = FontWeight.Bold,
                            color = GradientGreen
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GradientGreen,
                        focusedLabelColor = GradientGreen
                    )
                )

                // Description Field
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    leadingIcon = {
                        Icon(Icons.Default.Description, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GradientGreen,
                        focusedLabelColor = GradientGreen
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Add Expense Button
                GradientButton(
                    text = "Add Expense",
                    onClick = {
                        if (isFormValid) {
                            scope.launch {
                                try {
                                    val expense = ExpenseEntity(
                                        monthlyIncome = monthlyIncome.toDouble(),
                                        currency = selectedCurrency,
                                        month = selectedMonth,
                                        daysInMonth = daysInMonth,
                                        totalAmount = totalAmount.toDouble(),
                                        description = description,
                                        category = category,
                                        date = Date().time,
                                        userId = 1 // Default user ID, you can modify this based on your user system
                                    )
                                    repository.insertExpense(expense)
                                    snackbarMessage = "Expense added successfully!"
                                    showSnackbar = true

                                    // Clear form
                                    monthlyIncome = ""
                                    totalAmount = ""
                                    description = ""

                                    // Navigate back after a short delay
                                    kotlinx.coroutines.delay(1500)
                                    onNavigateBack()
                                } catch (e: Exception) {
                                    snackbarMessage = "Failed to add expense: ${e.message}"
                                    showSnackbar = true
                                }
                            }
                        } else {
                            snackbarMessage = "Please fill all required fields correctly"
                            showSnackbar = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isFormValid
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // snack bar
        if (showSnackbar) {
            LaunchedEffect(showSnackbar) {
                kotlinx.coroutines.delay(3000)
                showSnackbar = false
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (snackbarMessage.contains("success"))
                            GradientGreen else Color.Red
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = snackbarMessage,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}