package com.example.expenses.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expenses.Constants
import com.example.expenses.R
import com.example.expenses.repository.data.TransactionCollection

class MonthAdapter(private val mCallback: AdapterCallback, private val list: List<TransactionCollection>) : ListAdapter<TransactionCollection, MonthAdapter.MonthViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_month, parent)
        return MonthViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val item = list[position]
        holder.month.text = "${Constants.getMonth(item.month)}\n${item.month}"
        holder.total.text = Constants.numberFormat.format(item.balanceAmount)
        mCallback.setUpList(holder.expensesList, item.id)
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
        fun setUpList(recyclerView: RecyclerView, collectionId: Int)
    }
}