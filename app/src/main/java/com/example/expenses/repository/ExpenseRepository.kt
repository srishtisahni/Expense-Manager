package com.example.expenses.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions
import com.example.expenses.repository.room.ExpenseDatabase

class ExpenseRepository(context: Context) {
    private val database: ExpenseDatabase = ExpenseDatabase.getInstance(context)

    fun getCollection() : LiveData<List<TransactionCollection>> = database.getDao().getCollection()

    fun getTransactionsForExpense(expenseId: Int): LiveData<List<Transactions>> = database.getDao().getTransactionsForExpense(expenseId)

    fun getTransactionsForCollection(collectionId: Int): LiveData<List<Transactions>> = database.getDao().getTransactionsForCollection(collectionId)

    fun completeExpense(transaction: Transactions) {
        transaction.completed = true
        database.getDao().updateTransaction(transaction)
    }
}