package com.example.expenses.ui.callbacks

import android.widget.TextView
import com.example.expenses.repository.data.Reminders
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions
import com.example.expenses.ui.adapters.MonthAdapter
import com.example.expenses.ui.adapters.ReminderAdapter
import com.example.expenses.ui.adapters.TransactionAdapter
import com.example.expenses.ui.adapters.TransactionTextAdapter
import com.example.expenses.ui.fragments.MonthFragment

interface ActivityCallback {
    fun save(name: String, salary: Float, budget: Float)
    fun fetchReminders(reminders: MutableList<Reminders>, reminderAdapter: ReminderAdapter)
    fun fetchCollections(collections: MutableList<TransactionCollection>, monthAdapter: MonthAdapter)
    fun addNewCollection(): Boolean
    fun updateTransactions(collectionId: Long, transactions: MutableList<Transactions>, transactionTextAdapter: TransactionTextAdapter)
    fun setBalance(balanceAmount: TextView)
    fun navigateToRemindersFragment()
    fun addTransactionForCollection(transactions: Transactions)
    fun addReminder(reminders: Reminders)
    fun completeTransaction(reminder: Reminders)
    fun deleteTransaction(reminder: Reminders)
    fun fetchCollection(collectionId: Long, monthFragment: MonthFragment)
    fun navigateToMonth(id: Long)
    fun fetchTransactions(collectionId: Long, transactions: MutableList<Transactions>, adapter: TransactionAdapter, monthFragment: MonthFragment)
    fun navigateToAdd(collectionId: Long)
}