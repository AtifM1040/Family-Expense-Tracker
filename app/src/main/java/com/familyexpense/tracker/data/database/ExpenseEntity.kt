package com.familyexpense.tracker.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val monthlyIncome: Double,
    val currency: String,
    val month: String,
    val daysInMonth: Int,
    val totalAmount: Double,
    val description: String,
    val category: String,
    val date: Long = System.currentTimeMillis(),
    val userId: Long
)

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val email: String,
    val password: String
)