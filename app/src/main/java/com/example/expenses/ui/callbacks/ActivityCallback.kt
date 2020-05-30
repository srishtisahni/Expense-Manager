package com.example.expenses.ui.callbacks

import androidx.lifecycle.LiveData
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions

interface ActivityCallback {
    fun save(name: String, salary: Float, budget: Float)
    fun fetchBalance(): String
    fun fetchReminder(): LiveData<List<Transactions>>
    fun fetchCollections(): LiveData<List<TransactionCollection>>
    fun addNewCollection(): Boolean
}