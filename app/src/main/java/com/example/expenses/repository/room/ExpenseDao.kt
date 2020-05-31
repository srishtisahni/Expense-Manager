package com.example.expenses.repository.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.expenses.repository.data.Expense
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions

@Dao
interface ExpenseDao {
    @Query("SELECT * from Transactions where expenseTitle = :expenseTitle and date > :date and completed = :completed")
    fun getTransactions(expenseTitle: String, date: Long = 0, completed: Boolean = true): LiveData<List<Transactions>>

    @Query("SELECT * from Transactions where completed = :completed")
    fun getPendingTransactions(completed: Boolean = false): LiveData<List<Transactions>>

    @Query("SELECT * from Transactions where collectionId = :collectionId")
    fun getTransactions(collectionId: Long): LiveData<List<Transactions>>

    @Query("SELECT * from TransactionCollection")
    fun getCollections(): LiveData<List<TransactionCollection>>

    @Insert
    fun addCollection(collection: TransactionCollection): Long

    @Insert
    fun addExpense(expense: Expense)

    @Insert
    fun addTransaction(transaction: Transactions)

    @Update
    fun updateTransaction(transactions: Transactions)

    @Update
    fun updateCollection(collection: TransactionCollection)
}