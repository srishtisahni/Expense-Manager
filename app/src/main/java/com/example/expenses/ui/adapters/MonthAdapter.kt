package com.example.expenses.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expenses.Constants
import com.example.expenses.R
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Transactions

class MonthAdapter(private val mCallback: AdapterCallback, private val context: Context, private val list: List<TransactionCollection>) : ListAdapter<TransactionCollection, MonthAdapter.MonthViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_month, parent, false)
        return MonthViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val item = list[position]

        holder.month.text = "${Constants.getMonth(item.month)}\n${item.year}"
        holder.total.text = Constants.currencyFormat.format(item.balanceAmount)
        val transactionTextAdapter = TransactionTextAdapter(context, item.transactions, R.layout.list_item_text)

        with(holder.expensesList){
            adapter = transactionTextAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        holder.itemView.setOnClickListener {
            mCallback.monthSelected(item.id)
        }
        mCallback.setUpList(item, transactionTextAdapter)
    }

    override fun getItemCount(): Int = list.size

    class MonthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val month: TextView = itemView.findViewById(R.id.month)
        val expensesList: RecyclerView = itemView.findViewById(R.id.expenses)
        val total: TextView = itemView.findViewById(R.id.total)
    }

    class DiffCallback : DiffUtil.ItemCallback<TransactionCollection>() {
        override fun areItemsTheSame( oldItem: TransactionCollection, newItem: TransactionCollection): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TransactionCollection, newItem: TransactionCollection): Boolean = oldItem == newItem
    }

    interface AdapterCallback{
        fun setUpList(
            collection: TransactionCollection,
            transactionTextAdapter: TransactionTextAdapter
        )

        fun monthSelected(id: Long)
    }
}