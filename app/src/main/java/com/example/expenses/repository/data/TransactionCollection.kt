package com.example.expenses.repository.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.expenses.Constants

@Entity
data class TransactionCollection(
    val month: Int,
    val year:Int,
    var balanceAmount: Float = 0f
) {
    @Ignore
    val transactions: MutableList<Transactions> = mutableListOf<Transactions>()

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor(id: Long, month:Int, year:Int , balanceAmount: Float = 0f) : this(month, year , balanceAmount) {
        this.id = id
    }

    @Ignore
    fun updateBalance() {
        transactions.forEach {
            balanceAmount = 0f
            if(it.type == Constants.INCOME)
                balanceAmount+=it.amount
            else
                balanceAmount-=it.amount
        }
    }
}