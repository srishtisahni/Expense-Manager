package com.example.expenses.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.expenses.Constants
import com.example.expenses.repository.ExpenseRepository
import com.example.expenses.repository.data.Expense
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions
import com.example.expenses.repository.data.UserDetails
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPref: SharedPreferences
    private val repository: ExpenseRepository

    private val userDetails: MutableLiveData<UserDetails> = MutableLiveData()
    private val collectionTransactions: MutableMap<Long, LiveData<List<Transactions>>> = mutableMapOf()
    private lateinit var pendingTransactions: LiveData<List<Transactions>>
    private lateinit var collections: LiveData<List<TransactionCollection>>

    init {
        sharedPref = getApplication<Application>().getSharedPreferences(ACCOUNT_DETAILS, Context.MODE_PRIVATE)
        repository = ExpenseRepository.getInstance(getApplication())
        if(isOldUser())
            fetchUserDetails()
        else
            createExpense(SALARY)
    }

    private fun createExpense(name: String) {
        repository.addExpense(Expense(name))
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

    fun getReminders(): LiveData<List<Transactions>> {
        if(!this::pendingTransactions.isInitialized)
            pendingTransactions = repository.getPendingTransactions()
        return pendingTransactions
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
        val c = Calendar.getInstance()
        c.time = Date(timeInMillis)
        val month = c[Calendar.MONTH]
        val year = c[Calendar.YEAR]
        if(!sharedPref.getBoolean("$month/$year",false)){
            sharedPref.edit().putBoolean("$month/$year", true).apply()
            return repository.addCollection(TransactionCollection(month = month, year = year))
        }
        return null
    }

    fun addSalaryTransaction(collection: TransactionCollection) {
        val date = Calendar.getInstance()
        date.set(collection.year, collection.month, 1)
        val transactions = Transactions(SALARY, collection.id, "", userDetails.value!!.salary, date.timeInMillis,Constants.INCOME, true)
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


    companion object{
        private val ACCOUNT_DETAILS = "account_expenses"
        private val EXISTS = "exists"
        private val NAME = "name"
        private val SALARY = "Salary"
        private val BUDGET = "budget"
        private val BALANCE = "balance"
    }
}