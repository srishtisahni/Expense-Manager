package com.example.expenses.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenses.Constants
import com.example.expenses.R
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions
import com.example.expenses.ui.adapters.TransactionAdapter
import com.example.expenses.ui.callbacks.ActivityCallback

class CollectionFragment : Fragment() {

    private lateinit var rootLayout: View
    private lateinit var mCallback: ActivityCallback
    private var collectionId: Long? = null

    private lateinit var month: TextView
    private lateinit var progressSpent: ProgressBar
    private lateinit var spentAmount: TextView
    private lateinit var progressSave: ProgressBar
    private lateinit var saveAmount: TextView
    private lateinit var balanceAmount: TextView
    private lateinit var expenseList: RecyclerView
    private lateinit var addTransaction: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootLayout = inflater.inflate(R.layout.fragment_collection, container, false)

        with(rootLayout) {
            month = findViewById(R.id.month)
            progressSpent = findViewById(R.id.progressSpent)
            spentAmount = findViewById(R.id.spentAmount)
            progressSave = findViewById(R.id.progressSave)
            saveAmount = findViewById(R.id.saveAmount)
            balanceAmount = findViewById(R.id.balanceAmount)
            expenseList = findViewById(R.id.expenses)
            addTransaction = findViewById(R.id.addTransaction)
        }

        addTransaction.setOnClickListener {
            mCallback.navigateToAddFragment(collectionId!!)
        }

        mCallback.fetchCollection(collectionId!!, this)
        setUpAdapter()

        return rootLayout
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is ActivityCallback)
            mCallback = context

        collectionId = arguments?.getLong("collectionId")
    }

    fun updateDetails(collection: TransactionCollection) {
        month.text = "- ${Constants.getMonth(collection.month)}, ${collection.year} -"
        balanceAmount.text = "${Constants.currencyFormat.format(Constants.balance)}"
    }

    fun updateProgress(expense: Float, income: Float) {
        val balanceAmount: Float = income - expense
        val spent = (expense / income) * 100
        progressSave.progress = spent.toInt()
        spentAmount.text = "${Constants.currencyFormat.format(expense)} Spent"

        val saving = 100 - spent
        progressSave.progress = saving.toInt()
        saveAmount.text = "${Constants.currencyFormat.format(balanceAmount)} Saved"
    }


    private fun setUpAdapter() {
        val transactions = mutableListOf<Transactions>()
        with(expenseList) {
            val transactionsAdapter = TransactionAdapter( object: TransactionAdapter.ItemClickCallback {
                override fun onLongClick(transactions: Transactions) {
                    val builder = AlertDialog.Builder(context)
                    with(builder) {
                        setTitle("Delete Transaction")
                        setMessage("Do you want to delete the following Transaction?\n${transactions.expenseTitle}")
                        setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                            mCallback.deleteTransaction(transactions)
                        }
                        setNegativeButton("No") { _: DialogInterface, _: Int ->

                        }
                        show()
                    }
                }
            },transactions)
            adapter = transactionsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            mCallback.fetchTransactionsCollection(collectionId!!, transactions, transactionsAdapter, this@CollectionFragment)
        }
    }
}
