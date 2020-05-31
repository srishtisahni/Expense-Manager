package com.example.expenses.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.expenses.Constants
import com.example.expenses.R
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions
import com.example.expenses.repository.data.UserDetails
import com.example.expenses.ui.adapters.MonthAdapter
import com.example.expenses.ui.adapters.TransactionTextAdapter
import com.example.expenses.ui.callbacks.ActivityCallback
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

    private fun navigateTo(navigationId: Int) {
        navigationController.navigate(navigationId)
        with(supportActionBar!!) {
            show()
            setDisplayHomeAsUpEnabled(false)
            title = "Expenses"
        }
    }

    override fun onSupportNavigateUp() = navigationController.navigateUp()

    override fun save(name: String, salary: Float, budget: Float) {
        model.updateUserInfo(UserDetails( name, salary, budget, 0f))
        navigateTo(R.id.action_loginFragment_to_homeFragment)
    }

    override fun fetchReminders(
        reminders: MutableList<Transactions>,
        reminderAdapter: TransactionTextAdapter
    ){
        model.getReminders().observe(this, androidx.lifecycle.Observer {
            if(it != null) {
                reminders.clear()
                reminders.addAll(it)
                reminderAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun fetchCollections(
        collections: MutableList<TransactionCollection>,
        monthAdapter: MonthAdapter
    ) {
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
        val liveData = model.addCollection(Calendar.getInstance().timeInMillis)
        if(liveData == null){
            return false
        } else {
            liveData.observe(this, androidx.lifecycle.Observer {
                model.addSalaryTransaction(it)
            })
        }
        return true
    }

    override fun updateTransactions(
        collection: TransactionCollection,
        transactionTextAdapter: TransactionTextAdapter
    ){
        model.fetchTransactions(collection.id).observe(this, androidx.lifecycle.Observer {
            if (it!=null) {
                collection.transactions.clear()
                collection.transactions.addAll(it)
                model.updateCollectionCost(collection)
                transactionTextAdapter.notifyDataSetChanged()
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
}
