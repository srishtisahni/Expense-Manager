package com.example.expenses.repository.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.expenses.repository.data.Reminders
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions

@Dao
interface ExpenseDao {
    @Query("SELECT * from Transactions where expenseTitle = :expenseTitle")
    fun getTransactions(expenseTitle: String): LiveData<List<Transactions>>

    @Query("SELECT * from Reminders")
    fun getReminders(): LiveData<List<Reminders>>

    @Query("SELECT * from Transactions where collectionId = :collectionId")
    fun getTransactions(collectionId: Long): LiveData<List<Transactions>>

    @Query("SELECT * from TransactionCollection")
    fun getCollections(): LiveData<List<TransactionCollection>>

    @Insert
    fun addCollection(collection: TransactionCollection): Long

    @Insert
    fun addTransaction(transaction: Transactions): Long

    @Insert
    fun addReminder(reminder: Reminders): Long

    @Update
    fun updateTransaction(transactions: Transactions)

    @Update
    fun updateCollection(collection: TransactionCollection)

    @Delete
    fun deleteReminder(reminder: Reminders)
}