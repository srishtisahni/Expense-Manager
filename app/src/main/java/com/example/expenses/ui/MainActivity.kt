package com.example.expenses.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.expenses.R
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions
import com.example.expenses.repository.data.UserDetails
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

    override fun save(name: String, income: Float, budget: Float) {
        model.updateUserInfo(UserDetails( name, income, budget, 0f))
        navigateTo(R.id.action_loginFragment_to_homeFragment)
    }

    override fun fetchBalance(): String = model.userBalanceAsString()
    override fun fetchReminder(): LiveData<List<Transactions>> = model.getReminders()
    override fun fetchCollections(): LiveData<List<TransactionCollection>> = model.getCollections()
    override fun addNewCollection(): Boolean = model.addCollection(Calendar.getInstance().timeInMillis)
}
