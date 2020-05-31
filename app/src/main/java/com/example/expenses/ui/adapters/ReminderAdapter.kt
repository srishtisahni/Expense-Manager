package com.example.expenses.ui.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expenses.Constants
import com.example.expenses.R
import com.example.expenses.repository.data.Reminders
import com.example.expenses.repository.data.Transactions

class ReminderAdapter( private val mCallback: AdapterCallback, private val context: Context, private val list: List<Reminders>) : ListAdapter<Reminders, ReminderAdapter.TransactionTextViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionTextViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_reminder, parent, false) as TextView
        return TransactionTextViewHolder(textView)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: TransactionTextViewHolder, position: Int) {
        val item = list[position]
        if(item.type == Constants.EXPENSE) {
            holder.textView.setTextColor(context.getColor(R.color.colorExpense))
        }
        else if(item.type == Constants.INCOME) {
            holder.textView.setTextColor(context.getColor(R.color.colorIncome))
        }
        holder.textView.text = "*${item.expenseTitle} - ${Constants.dateFormat.format(item.date)} (${Constants.currencyFormat.format(item.amount)})"
        holder.textView.setOnClickListener {
            mCallback.onClick(item)
        }
        holder.textView.setOnLongClickListener {
            mCallback.onLongClick(item)
            true
        }
    }

    override fun getItemCount(): Int = list.size

    class TransactionTextViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView)

    class DiffCallback: DiffUtil.ItemCallback<Reminders>() {
        override fun areItemsTheSame(oldItem: Reminders, newItem: Reminders): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Reminders, newItem: Reminders): Boolean = oldItem == newItem
    }

    interface AdapterCallback{
        fun onClick(reminder: Reminders)
        fun onLongClick(reminder: Reminders)
    }
}
