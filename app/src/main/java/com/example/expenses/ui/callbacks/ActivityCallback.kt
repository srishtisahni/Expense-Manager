package com.example.expenses.ui.callbacks

import android.widget.TextView
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions
import com.example.expenses.ui.adapters.MonthAdapter
import com.example.expenses.ui.adapters.TransactionTextAdapter

interface ActivityCallback {
    fun save(name: String, salary: Float, budget: Float)
    fun fetchReminders(reminders: MutableList<Transactions>, reminderAdapter: TransactionTextAdapter)
    fun fetchCollections(collections: MutableList<TransactionCollection>, monthAdapter: MonthAdapter)
    fun addNewCollection(): Boolean
    fun updateTransactions(collection: TransactionCollection, transactionTextAdapter: TransactionTextAdapter)
    fun setBalance(balanceAmount: TextView)
}