package com.example.expenses.repository

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.expenses.Constants
import com.example.expenses.repository.data.Reminders
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions
import com.example.expenses.repository.room.ExpenseDatabase

class ExpenseRepository private constructor(val context: Context) {

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


    fun addCollection(transactionCollection: TransactionCollection) : LiveData<TransactionCollection> {
        val transaction = MutableLiveData<TransactionCollection>()
        dbHandler.post {
            val newId = database.getDao().addCollection(transactionCollection)
            transactionCollection.id = newId
            transaction.postValue(transactionCollection)
        }
        return transaction
    }

    fun addTransaction(transaction: Transactions) {
        dbHandler.post {
            database.getDao().addTransaction(transaction)
            updateExpense(transaction.collectionId)
        }
    }

    fun addReminder(reminder: Reminders) {
        dbHandler.post {
            database.getDao().addReminder(reminder)
        }
    }

    fun getReminders(): LiveData<List<Reminders>> = database.getDao().getReminders()

    fun getTransactions(expenseTitle: String): LiveData<List<Transactions>> = database.getDao().getTransactions(expenseTitle)

    fun getTransactions(collectionId: Long): LiveData<List<Transactions>> = database.getDao().getTransactions(collectionId)

    fun getCollections() : LiveData<List<TransactionCollection>> = database.getDao().getCollections()

    fun getCollection(collectionId: Long): LiveData<TransactionCollection> = database.getDao().getCollectionLive(collectionId)

    private fun updateExpense(collectionId: Long) {
        val income = database.getDao().getBalanceFromType(collectionId, Constants.INCOME)
        val expense = database.getDao().getBalanceFromType(collectionId, Constants.EXPENSE)
        val collection = database.getDao().getCollection(collectionId)
        collection.balanceAmount = income - expense
        database.getDao().updateCollection(collection)
    }

    fun deleteReminder(reminder: Reminders) {
        dbHandler.post {
            database.getDao().deleteReminder(reminder)
        }
    }

    fun deleteTransaction(transaction: Transactions) {
        dbHandler.post {
            database.getDao().deleteTransaction(transaction)
            updateExpense(transaction.collectionId)
        }
    }

}