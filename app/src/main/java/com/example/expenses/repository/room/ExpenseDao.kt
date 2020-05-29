package com.example.expenses.repository.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions

@Dao
interface ExpenseDao {
    @Query("SELECT * from Transactions where expenseId = :expenseId")
    fun getTransactionsForExpense(expenseId: Int): LiveData<List<Transactions>>

    @Query("SELECT * from Transactions where collectionId = :collectionId")
    fun getTransactionsForCollection(collectionId: Int): LiveData<List<Transactions>>

    @Query("SELECT * from TransactionCollection")
    fun getCollection(): LiveData<List<TransactionCollection>>

    @Update
    fun updateTransaction(transactions: Transactions)
}