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

class TransactionTextAdapter(private val context: Context, private val list: List<Transactions>) : ListAdapter<Transactions, TransactionTextAdapter.TransactionTextViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionTextViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_text, parent, false) as TextView
        return TransactionTextViewHolder(textView)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: TransactionTextViewHolder, position: Int) {
        val item = list[position]
        var symbol = ""
        if(item.type == Constants.EXPENSE) {
            holder.textView.setTextColor(context.getColor(R.color.colorExpense))
            symbol = "-"
        }
        else if(item.type == Constants.INCOME) {
            holder.textView.setTextColor(context.getColor(R.color.colorIncome))
            symbol = "+"
        }
        holder.textView.text = "$symbol${Constants.currencyFormat.format(item.amount)}"
    }

    override fun getItemCount(): Int = list.size

    class TransactionTextViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView)

    class DiffCallback: DiffUtil.ItemCallback<Transactions>() {
        override fun areItemsTheSame(oldItem: Transactions, newItem: Transactions): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Transactions, newItem: Transactions): Boolean = oldItem == newItem
    }
}
