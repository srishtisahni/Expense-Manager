package com.example.expenses.ui.callbacks

import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.expenses.repository.data.Reminders
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions
import com.example.expenses.ui.adapters.*
import com.example.expenses.ui.fragments.CollectionFragment

interface ActivityCallback {
    fun saveUserDetails(name: String, salary: Float, budget: Float)
    fun setBalance(balanceAmount: TextView)

    fun addNewCollection(): Boolean
    fun fetchCollections(collections: MutableList<TransactionCollection>, collectionAdapter: CollectionAdapter)
    fun fetchCollection(collectionId: Long, collectionFragment: CollectionFragment)

    fun addReminder(reminders: Reminders)
    fun fetchReminders(reminders: MutableList<Reminders>, reminderAdapter: ReminderAdapter)
    fun completeTransaction(reminder: Reminders)
    fun deleteReminder(reminder: Reminders)

    fun addTransactionForCollection(transactions: Transactions)
    fun fetchTransactionsForHome(collectionId: Long, transactions: MutableList<Transactions>, transactionTextAdapter: TransactionTextAdapter)
    fun fetchTransactionsCollection(collectionId: Long, transactions: MutableList<Transactions>, adapter: TransactionAdapter, monthFragment: CollectionFragment)
    fun deleteTransaction(transactions: Transactions)

    fun navigateToRemindersFragment()
    fun navigateToCollectionFragment(collectionId: Long)
    fun navigateToAddFragment(collectionId: Long)
}