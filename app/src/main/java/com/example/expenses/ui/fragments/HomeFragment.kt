package com.example.expenses.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.expenses.R
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions
import com.example.expenses.ui.adapters.MonthAdapter
import com.example.expenses.ui.adapters.ReminderAdapter
import com.example.expenses.ui.callbacks.ActivityCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), MonthAdapter.AdapterCallback {

    private lateinit var rootLayout: View
    private lateinit var balanceAmount: TextView
    private lateinit var remindersList: RecyclerView
    private lateinit var addReminder: FloatingActionButton
    private lateinit var addMonth: CardView
    private lateinit var monthsList: RecyclerView

    private lateinit var mCallback: ActivityCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootLayout = inflater.inflate(R.layout.fragment_home, container, false)

        with(rootLayout) {
            balanceAmount = findViewById(R.id.balanceAmount)
            remindersList = findViewById(R.id.reminders)
            addReminder = findViewById(R.id.addReminder)
            addMonth = findViewById(R.id.addMonth)
            monthsList = findViewById(R.id.monthList)
        }

        return rootLayout
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is ActivityCallback)
            mCallback = context

        setUpAdapters(context)
        setUpClickListeners()
        balanceAmount.text = mCallback.fetchBalance()
    }

    private fun setUpAdapters(context: Context) {
        //reminders
        val reminders = mutableListOf<Transactions>()
        val reminderAdapter = ReminderAdapter(context, reminders)
        mCallback.fetchReminder().observe(this, Observer {
            if(it != null) {
                reminders.clear()
                reminders.addAll(it)
                reminderAdapter.notifyDataSetChanged()
            }
        })
        with(remindersList) {
            adapter = reminderAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        //months
        val collections = mutableListOf<TransactionCollection>()
        val monthAdapter = MonthAdapter(this, collections)
        mCallback.fetchCollections().observe(this, Observer {
            if(it!=null) {
                collections.clear()
                collections.addAll(it)
                monthAdapter.notifyDataSetChanged()
            }
        })
        with(monthsList){
            adapter = monthAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setUpClickListeners() {
        addMonth.setOnClickListener {
            if(!mCallback.addNewCollection())
                Toast.makeText(context, "This month already exists as a collection", Toast.LENGTH_SHORT).show()
        }

        addReminder.setOnClickListener {
            //TODO Add Reminders
        }
    }

    override fun setUpList(recyclerView: RecyclerView, collectionId: Int) {
        //TODO Add List
    }

}
