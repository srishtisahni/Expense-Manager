package com.example.expenses.repository

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.expenses.repository.data.Expense
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions
import com.example.expenses.repository.data.UserDetails
import com.example.expenses.repository.room.ExpenseDatabase

class ExpenseRepository private constructor(context: Context) {

    private val database: ExpenseDatabase = ExpenseDatabase.getInstance(context)
    private val handlerThread: HandlerThread = HandlerThread("DatabaseHandlerThread").also{ it.start() }
    private val dbHandler = Handler(handlerThread.looper)


    companion object{
        lateinit var INSTANCE: ExpenseRepository

        fun getInstance(context: Context): ExpenseRepository {
            if(!this::INSTANCE.isInitialized)
                INSTANCE = ExpenseRepository(context)
            return INSTANCE
        }
    }

    fun getCollections() : LiveData<List<TransactionCollection>> = database.getDao().getCollections()

    fun getTransactionsForExpense(expenseTitle: String): LiveData<List<Transactions>> = database.getDao().getTransactions(expenseTitle)

    fun getTransactions(collectionId: Long): LiveData<List<Transactions>> = database.getDao().getTransactions(collectionId)

    fun getPendingTransactions(): LiveData<List<Transactions>> = database.getDao().getPendingTransactions()

    fun completeExpense(transaction: Transactions) {
        dbHandler.post {
            transaction.completed = true
            database.getDao().updateTransaction(transaction)
        }
    }

    fun addCollection(transactionCollection: TransactionCollection) : LiveData<TransactionCollection> {
        val id = MutableLiveData<TransactionCollection>()
        dbHandler.post {
            val newId = database.getDao().addCollection(transactionCollection)
            transactionCollection.id = newId
            id.postValue(transactionCollection)
        }
        return id
    }

    fun addTransaction(transaction: Transactions) {
        dbHandler.post {
            database.getDao().addTransaction(transaction)
        }
    }

    fun addExpense(expense: Expense) {
        dbHandler.post {
            database.getDao().addExpense(expense)
        }
    }

    fun updateCollection(collection: TransactionCollection) {
        dbHandler.post {
            database.getDao().updateCollection(collection)
        }
    }
}