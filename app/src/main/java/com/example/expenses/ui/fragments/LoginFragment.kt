package com.example.expenses.ui.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.example.expenses.R
import com.example.expenses.ui.callbacks.ActivityCallback as ActivityCallback

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private lateinit var rootLayout: View
    private lateinit var name: EditText
    private lateinit var salary: EditText
    private lateinit var budget: EditText
    private lateinit var save: Button
    private lateinit var mCallback: ActivityCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootLayout = inflater.inflate(R.layout.fragment_login, container, false)

        name = rootLayout.findViewById(R.id.name)
        salary = rootLayout.findViewById(R.id.salary)
        budget = rootLayout.findViewById(R.id.budget)
        save = rootLayout.findViewById(R.id.save)

        save.setOnClickListener {
            val nameEntry = name.text.toString()
            val incomeEntry = salary.text.toString().toFloatOrNull()
            val budgetEntry = budget.text.toString().toFloatOrNull()

            if(nameEntry.isNotEmpty() && incomeEntry!=null && budgetEntry!=null)
                mCallback.save(nameEntry, incomeEntry, budgetEntry)
            else
                Toast.makeText(context, "INVALID INPUT", Toast.LENGTH_SHORT).show()
        }

        return rootLayout
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is ActivityCallback)
            mCallback = context
    }
}
