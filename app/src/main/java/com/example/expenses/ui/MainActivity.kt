package com.example.expenses.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.expenses.Constants
import com.example.expenses.R
import com.example.expenses.repository.ExpenseRepository
import com.example.expenses.repository.data.Reminders
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions
import com.example.expenses.repository.data.UserDetails
import com.example.expenses.ui.adapters.MonthAdapter
import com.example.expenses.ui.adapters.ReminderAdapter
import com.example.expenses.ui.adapters.TransactionAdapter
import com.example.expenses.ui.adapters.TransactionTextAdapter
import com.example.expenses.ui.callbacks.ActivityCallback
import com.example.expenses.ui.fragments.MonthFragment
import com.example.expenses.viewmodels.MainViewModel
import java.util.*


class MainActivity : AppCompatActivity(), ActivityCallback {

    private lateinit var navigationController: NavController
    private lateinit var model: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = ViewModelProvider(this) [MainViewModel::class.java]

        navigationController = findNavController(R.id.navigationHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navigationController)

        if(model.isOldUser())
            navigateTo(R.id.action_loginFragment_to_homeFragment)
        else {
            supportActionBar?.hide()
        }
    }

    private fun navigateTo(navigationId: Int, bundle: Bundle? = null) {
        navigationController.navigate(navigationId, bundle)
        when(navigationId) {
            R.id.action_monthFragment_to_addFragment -> setAppTitle(R.string.add_transaction)
            R.id.action_homeFragment_to_addFragment -> {
                setAppTitle(R.string.add_reminder)
            }
            else -> {
                setAppTitle(R.string.app_name)
            }
        }
    }

    private fun setAppTitle(stringId: Int) {
        with(supportActionBar!!) {
            show()
            setDisplayHomeAsUpEnabled(false)
            title = getString(stringId)
        }
    }

    override fun onSupportNavigateUp() = navigationController.navigateUp()

    override fun save(name: String, salary: Float, budget: Float) {
        model.updateUserInfo(UserDetails( name, salary, budget, 0f))
        navigateTo(R.id.action_loginFragment_to_homeFragment)
    }

    override fun fetchReminders(reminders: MutableList<Reminders>, reminderAdapter: ReminderAdapter) {
        model.getReminders().observe(this, androidx.lifecycle.Observer {
            if(it != null) {
                reminders.clear()
                reminders.addAll(it)
                reminderAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun fetchCollections(collections: MutableList<TransactionCollection>, monthAdapter: MonthAdapter) {
        model.getCollections().observe(this, androidx.lifecycle.Observer {
            if(it!=null) {
                collections.clear()
                collections.addAll(it)
                monthAdapter.notifyDataSetChanged()
                model.updateBalance(it)
            }
        })
    }

    override fun addNewCollection(): Boolean {
        val calendar = Calendar.getInstance()
        calendar[Calendar.DATE] = 1
        val liveData = model.addCollection(calendar.timeInMillis)
        if(liveData == null){
            return false
        } else {
            liveData.observe(this, androidx.lifecycle.Observer {
                model.addSalaryTransaction(it)
            })
        }
        return true
    }

    override fun updateTransactions(collectionId: Long, transactions: MutableList<Transactions>, transactionTextAdapter: TransactionTextAdapter) {
        model.fetchTransactions(collectionId).observe(this, androidx.lifecycle.Observer {
            if (it!=null) {
                transactions.clear()
                transactions.addAll(it)
                transactionTextAdapter.notifyDataSetChanged()
            }
        })
    }


    override fun fetchTransactions(collectionId: Long, transactions: MutableList<Transactions>, adapter: TransactionAdapter, monthFragment: MonthFragment) {
        model.fetchTransactions(collectionId).observe(this, androidx.lifecycle.Observer {
            if (it!=null) {
                transactions.clear()
                transactions.addAll(it)
                var income = 0f
                var expense = 0f
                transactions.forEach {transaction ->
                    if(transaction.type == Constants.INCOME)
                        income += transaction.amount
                    else
                        expense += transaction.amount
                }
                monthFragment.updateProgress(expense, income)
                adapter.notifyDataSetChanged()
            }
        })
    }

    override fun setBalance(balanceAmount: TextView) {
        model.getUser().observe(this, androidx.lifecycle.Observer {
            if(it!=null) {
                balanceAmount.text = Constants.currencyFormat.format(it.balance)
            }
        })
    }

    override fun navigateToRemindersFragment() {
        navigateTo(R.id.action_homeFragment_to_addFragment)
    }

    override fun addTransactionForCollection(transactions: Transactions) {
        model.addTransaction(transactions)
        navigationController.popBackStack(R.id.monthFragment, false)
        setAppTitle(R.string.app_name)

    }

    override fun addReminder(reminders: Reminders) {
        model.addReminder(reminders)
        navigationController.popBackStack(R.id.homeFragment, false)
        setAppTitle(R.string.app_name)
    }

    override fun completeTransaction(reminder: Reminders) {
        model.completeTransaction(reminder)
    }

    override fun deleteTransaction(reminder: Reminders) {
        model.deleteTransaction(reminder)
    }

    override fun fetchCollection(collectionId: Long, monthFragment: MonthFragment){
        model.fetchCollection(collectionId).observe(this, androidx.lifecycle.Observer {
            monthFragment.updateDetails(it)
        })
    }

    override fun navigateToMonth(id: Long) {
        navigateTo(R.id.action_homeFragment_to_monthFragment, bundleOf("collectionId" to id))
    }

    override fun navigateToAdd(collectionId: Long) {
        navigateTo(R.id.action_monthFragment_to_addFragment, bundleOf("collectionId" to collectionId))
    }

    override fun onBackPressed() {
        val current = navigationController.currentDestination
        if(current?.id == R.id.monthFragment)
            navigationController.popBackStack(R.id.homeFragment, false)
        else
            Log.e("NAME", current?.navigatorName)
    }
}
