package com.example.expenses.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.expenses.Constants
import com.example.expenses.repository.ExpenseRepository
import com.example.expenses.repository.data.Reminders
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions
import com.example.expenses.repository.data.UserDetails
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPref: SharedPreferences
    private val repository: ExpenseRepository

    private val userDetails: MutableLiveData<UserDetails> = MutableLiveData()
    private val collectionTransactions: MutableMap<Long, LiveData<List<Transactions>>> = mutableMapOf()
    private lateinit var reminders: LiveData<List<Reminders>>
    private lateinit var collections: LiveData<List<TransactionCollection>>

    init {
        sharedPref = getApplication<Application>().getSharedPreferences(ACCOUNT_DETAILS, Context.MODE_PRIVATE)
        repository = ExpenseRepository.getInstance(getApplication())
        if(isOldUser())
            fetchUserDetails()
    }

    private fun fetchUserDetails() {
        userDetails.value = UserDetails(
            sharedPref.getString(NAME, null),
            sharedPref.getFloat(SALARY, 0f),
            sharedPref.getFloat(BUDGET, 0f),
            sharedPref.getFloat(BALANCE, 0f)
        )
    }

    fun isOldUser(): Boolean = sharedPref.getBoolean(EXISTS, false)

    fun updateUserInfo(userDetails: UserDetails) {
        this.userDetails.value = userDetails

        with(sharedPref.edit()) {
            putString(NAME, userDetails.name)
            putFloat(SALARY, userDetails.salary)
            putFloat(BUDGET, userDetails.budget)
            putFloat(BALANCE, userDetails.balance)
            putBoolean(EXISTS, true)
            commit()
        }
    }

    fun getReminders(): LiveData<List<Reminders>> {
        if(!this::reminders.isInitialized)
            reminders = repository.getReminders()
        return reminders
    }

    fun getCollections(): LiveData<List<TransactionCollection>> {
        if(!this::collections.isInitialized)
            collections = repository.getCollections()
        return collections
    }

    fun fetchTransactions(collectionId: Long): LiveData<List<Transactions>> {
        if(collectionTransactions[collectionId] == null)
            collectionTransactions[collectionId] = repository.getTransactions(collectionId)
        return collectionTransactions[collectionId]!!
    }

    fun addCollection(timeInMillis: Long): LiveData<TransactionCollection>? {
        val calendar = Calendar.getInstance()
        calendar.time = Date(timeInMillis)
        if(!sharedPref.getBoolean("${calendar[Calendar.MONTH]}/${calendar[Calendar.YEAR]}",false)){
            sharedPref.edit().putBoolean("${calendar[Calendar.MONTH]}/${calendar[Calendar.YEAR]}",true).apply()
            return repository.addCollection(TransactionCollection(calendar[Calendar.MONTH], calendar[Calendar.YEAR]))
        }
        return null
    }

    fun addSalaryTransaction(collection: TransactionCollection) {
        sharedPref.edit().putLong("${collection.month}/${collection.year}id",collection.id).apply()
        val calendar = Calendar.getInstance()
        calendar.set(collection.year,collection.month, 1)
        val transactions = Transactions(SALARY, collection.id, "", userDetails.value!!.salary, calendar.timeInMillis,Constants.INCOME)
        repository.addTransaction(transactions)
    }

    fun updateCollectionCost(collection: TransactionCollection) {
        collection.updateBalance()
        repository.updateCollection(collection)
    }

    fun updateBalance(it: List<TransactionCollection>) {
        var balance = 0f
        it.forEach { collection ->
            balance += collection.balanceAmount
        }
        val userDetails = this.userDetails.value!!
        userDetails.balance = balance
        this.userDetails.value = userDetails
    }

    fun getUser(): LiveData<UserDetails> = userDetails

    fun addTransaction(transaction: Transactions) {
        repository.addTransaction(transaction)
    }

    fun addReminder(reminder: Reminders) {
        repository.addReminder(reminder)
    }

    fun completeTransaction(reminder: Reminders) {
        val calendar = Calendar.getInstance()
        repository.deleteReminder(reminder)
        val transactions = Transactions(reminder, sharedPref.getLong("${calendar[Calendar.MONTH]}/${calendar[Calendar.YEAR]}id",-1))
        repository.addTransaction(transactions)
    }


    companion object{
        private val ACCOUNT_DETAILS = "account_expenses"
        private val EXISTS = "exists"
        private val NAME = "name"
        private val SALARY = "Salary"
        private val BUDGET = "budget"
        private val BALANCE = "balance"
    }
}