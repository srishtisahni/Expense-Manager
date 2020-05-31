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

    @Query("SELECT * from Reminders ORDER BY date")
    fun getReminders(): LiveData<List<Reminders>>

    @Query("SELECT * from Transactions where collectionId = :collectionId order by Date")
    fun getTransactions(collectionId: Long): LiveData<List<Transactions>>

    @Query("SELECT * from TransactionCollection")
    fun getCollections(): LiveData<List<TransactionCollection>>

    @Query("SELECT * from TransactionCollection where id = :collectionId")
    fun getCollection(collectionId: Long): TransactionCollection

    @Query("SELECT * from TransactionCollection where id = :collectionId")
    fun getCollectionLive(collectionId: Long): LiveData<TransactionCollection>

    @Query("SELECT SUM(amount) from Transactions where type = :type and collectionId = :collectionId")
    fun getBalanceFromType(collectionId: Long, type: Int): Float

    @Update
    fun updateCollection(collection: TransactionCollection)

    @Insert
    fun addCollection(collection: TransactionCollection): Long

    @Insert
    fun addTransaction(transaction: Transactions): Long

    @Insert
    fun addReminder(reminder: Reminders): Long

    @Delete
    fun deleteReminder(reminder: Reminders)
}