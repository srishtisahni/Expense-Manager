package com.example.expenses.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions
import com.example.expenses.repository.room.ExpenseDatabase

class ExpenseRepository(context: Context) {
    private val database: ExpenseDatabase = ExpenseDatabase.getInstance(context)

    fun getCollection() : LiveData<List<TransactionCollection>> = database.getDao().getCollection()

    fun getTransactionsForExpense(expenseTitle: String): LiveData<List<Transactions>> = database.getDao().getTransactions(expenseTitle)

    fun getTransactionsForCollection(collectionId: Int): LiveData<List<Transactions>> = database.getDao().getTransactions(collectionId)

    fun getPendingTransactions(): LiveData<List<Transactions>> = database.getDao().getPendingTransactions()

    fun completeExpense(transaction: Transactions) {
        transaction.completed = true
        database.getDao().updateTransaction(transaction)
    }

    fun addCollection(transactionCollection: TransactionCollection) {
        database.getDao().addCollection(transactionCollection)
    }
}