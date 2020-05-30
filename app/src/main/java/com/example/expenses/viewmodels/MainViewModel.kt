package com.example.expenses.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.expenses.Constants
import com.example.expenses.repository.ExpenseRepository
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions
import com.example.expenses.repository.data.UserDetails
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPref: SharedPreferences
    private val repository: ExpenseRepository = ExpenseRepository(getApplication())
    private lateinit var userDetails: UserDetails

    init {
        sharedPref = getApplication<Application>().getSharedPreferences(ACCOUNT_DETAILS, Context.MODE_PRIVATE)
        if(isOldUser())
            fetchUserDetails()
    }

    private fun fetchUserDetails() {
        userDetails = UserDetails(
            sharedPref.getString(NAME, null),
            sharedPref.getFloat(INCOME, 0f),
            sharedPref.getFloat(BUDGET, 0f),
            sharedPref.getFloat(BALANCE, 0f)
        )
    }

    fun isOldUser(): Boolean = sharedPref.getBoolean(EXISTS, false)

    fun updateUserInfo(userDetails: UserDetails) {
        this.userDetails = userDetails

        with(sharedPref.edit()) {
            putString(NAME, userDetails.name)
            putFloat(INCOME, userDetails.income)
            putFloat(BUDGET, userDetails.budget)
            putFloat(BALANCE, userDetails.balance)
            putBoolean(EXISTS, true)
            commit()
        }
    }

    fun userBalanceAsString(): String = Constants.numberFormat.format(userDetails.balance)
    fun getReminders(): LiveData<List<Transactions>> = repository.getPendingTransactions()
    fun getCollections(): LiveData<List<TransactionCollection>> = repository.getCollection()
    fun addCollection(timeInMillis: Long): Boolean {
        val c = Calendar.getInstance()
        c.time = Date(timeInMillis)
        val month = c[Calendar.MONTH]
        val year = c[Calendar.YEAR]
        if(!sharedPref.getBoolean("$month/$year",false)){
            sharedPref.edit().putBoolean("$month/$year", true).apply()
            repository.addCollection(TransactionCollection(month = month, year = year))
            return true
        }
        return false
    }


    companion object{
        private val ACCOUNT_DETAILS = "account_expenses"
        private val EXISTS = "exists"
        private val NAME = "name"
        private val INCOME = "income"
        private val BUDGET = "budget"
        private val BALANCE = "balance"
    }
}