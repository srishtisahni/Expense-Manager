package com.example.expenses.ui.fragments

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.expenses.Constants
import com.example.expenses.R
import com.example.expenses.repository.data.Reminders
import com.example.expenses.repository.data.Transactions
import com.example.expenses.ui.callbacks.ActivityCallback
import java.util.*


class AddFragment : Fragment() {

    private lateinit var rootLayout: View
    private lateinit var mCallback: ActivityCallback
    private var collectionId: Long? = null
    private lateinit var calendarInstance: Calendar

    private lateinit var title: EditText
    private lateinit var amount: EditText
    private lateinit var date: TextView
    private lateinit var comment: EditText
    private lateinit var income: Button
    private lateinit var expense: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootLayout = inflater.inflate(R.layout.fragment_add, container, false)

        calendarInstance = Calendar.getInstance()

        with(rootLayout) {
            title = findViewById(R.id.title)
            amount = findViewById(R.id.amount)
            date = findViewById(R.id.date)
            comment = findViewById(R.id.comments)
            income = findViewById(R.id.add_income)
            expense = findViewById(R.id.add_expense)
        }

        setUpListeners()
        return rootLayout
    }

    private fun setUpListeners() {
        val startTime = DatePickerDialog(
            context!!,
            OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendarInstance[year, monthOfYear] = dayOfMonth
                date.text = Constants.dateFormat.format(Date(calendarInstance.timeInMillis))
            },
            calendarInstance[Calendar.YEAR],
            calendarInstance[Calendar.MONTH],
            calendarInstance[Calendar.DAY_OF_MONTH]
        )
        date.setOnClickListener {
            startTime.show()
        }
        income.setOnClickListener {
            saveValues(Constants.INCOME)
        }
        expense.setOnClickListener {
            saveValues(Constants.EXPENSE)
        }
    }

    private fun saveValues(type: Int) {
        if(collectionId != null) {
            val transactions = Transactions(title.text.toString(),
                collectionId!!,
                comment.text.toString(),
                amount.text.toString().toFloat(),
                calendarInstance.timeInMillis,
                type
            )
            mCallback.addTransactionForCollection(transactions)
        } else {
            val reminders = Reminders(title.text.toString(),
                comment.text.toString(),
                amount.text.toString().toFloat(),
                calendarInstance.timeInMillis,
                type)
            mCallback.addReminder(reminders)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is ActivityCallback)
            mCallback = context
        collectionId = arguments?.getLong("collectionId")
    }
}