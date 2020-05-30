package com.example.expenses.ui.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expenses.Constants
import com.example.expenses.R
import com.example.expenses.repository.data.Transactions

class ReminderAdapter(private val context: Context, private val list: List<Transactions>) : ListAdapter<Transactions, ReminderAdapter.ReminderViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_notification, parent) as TextView
        return ReminderViewHolder(textView)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val item = list[position]
        if(item.type == Constants.EXPENSE)
            holder.textView.setTextColor(context.getColor(R.color.colorTextExpense))
        else if(item.type == Constants.INCOME)
            holder.textView.setTextColor(context.getColor(R.color.colorTextIncome))
        holder.textView.text = "* ${item.expenseTitle} - ${Constants.dateFormat.format(item.date)}"
    }

    override fun getItemCount(): Int = list.size

    class ReminderViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView)

    class DiffCallback: DiffUtil.ItemCallback<Transactions>() {
        override fun areItemsTheSame(oldItem: Transactions, newItem: Transactions): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Transactions, newItem: Transactions): Boolean = oldItem == newItem
    }
}
