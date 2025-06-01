package com.familyexpense.tracker.data.repository

import com.familyexpense.tracker.data.database.ExpenseDao
import com.familyexpense.tracker.data.database.ExpenseEntity
import com.familyexpense.tracker.data.database.UserDao
import com.familyexpense.tracker.data.database.UserEntity
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val userDao: UserDao
) {
    // User operations
    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    // Expense operations
    fun getAllExpenses(): Flow<List<ExpenseEntity>> {
        return expenseDao.getAllExpenses()
    }

    suspend fun insertExpense(expense: ExpenseEntity) {
        expenseDao.insertExpense(expense)
    }

    suspend fun deleteExpense(expense: ExpenseEntity) {
        expenseDao.deleteExpense(expense)
    }

    suspend fun getTotalExpenses(): Double {
        return expenseDao.getTotalExpenses() ?: 0.0
    }

    suspend fun getExpenseById(id: Long): ExpenseEntity? {
        return expenseDao.getExpenseById(id)
    }
    suspend fun loginUser(email: String, password: String): UserEntity? {
        return userDao.getUserByEmailAndPassword(email, password)
    }
}
