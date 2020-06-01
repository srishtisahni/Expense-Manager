package com.example.expenses.ui.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expenses.Constants
import com.example.expenses.R
import com.example.expenses.repository.data.Transactions
import java.util.*

class TransactionAdapter(private val mCallback: ItemClickCallback, private val list: List<Transactions>) : ListAdapter<Transactions, TransactionAdapter.TransactionViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return TransactionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val item = list[position]
        holder.itemView.setOnLongClickListener {
            mCallback.onLongClick(item)
            true
        }

        if(item.type == Constants.INCOME) {
            holder.expenseCard.visibility = View.GONE
            holder.incomeAmount.text = Constants.currencyFormat.format(item.amount)
            holder.incomeTitle.text = item.expenseTitle
            if(item.comments.isEmpty())
                holder.incomeComment.visibility = View.GONE
            else
                holder.incomeComment.text = item.comments
            holder.incomeDate.text = Constants.dateFormat.format(Date(item.date))
        } else {
            holder.incomeCard.visibility = View.GONE
            holder.expenseAmount.text = Constants.currencyFormat.format(item.amount)
            holder.expenseTitle.text = item.expenseTitle
            if(item.comments.isEmpty())
                holder.expenseComment.visibility = View.GONE
            else
                holder.expenseComment.text = item.comments
            holder.expenseDate.text = Constants.dateFormat.format(Date(item.date))
        }
    }

    override fun getItemCount(): Int = list.size

    class TransactionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val incomeCard: CardView = itemView.findViewById(R.id.incomeCard)
        val incomeTitle: TextView = itemView.findViewById(R.id.incomeItem)
        val incomeDate: TextView = itemView.findViewById(R.id.incomeDate)
        val incomeAmount: TextView = itemView.findViewById(R.id.incomeAmount)
        val incomeComment: TextView = itemView.findViewById(R.id.incomeComments)

        val expenseCard: CardView = itemView.findViewById(R.id.expenseCard)
        val expenseTitle: TextView = itemView.findViewById(R.id.expenseItem)
        val expenseDate: TextView = itemView.findViewById(R.id.expenseDate)
        val expenseAmount: TextView = itemView.findViewById(R.id.expenseAmount)
        val expenseComment: TextView = itemView.findViewById(R.id.expenseComments)

        init {
            incomeCard.setBackgroundResource(R.drawable.card_item_income)
            expenseCard.setBackgroundResource(R.drawable.card_item_expense)
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<Transactions>() {
        override fun areItemsTheSame(oldItem: Transactions, newItem: Transactions): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Transactions, newItem: Transactions): Boolean = oldItem == newItem
    }

    interface ItemClickCallback {
        fun onLongClick(transactions: Transactions)
    }
}
