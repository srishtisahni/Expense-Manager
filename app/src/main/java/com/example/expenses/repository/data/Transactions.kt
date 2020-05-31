package com.example.expenses.repository.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(
    ForeignKey(entity = Expense::class,
    parentColumns = arrayOf("name"),
    childColumns = arrayOf("expenseTitle"),
    onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = TransactionCollection::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("collectionId"),
        onDelete = ForeignKey.CASCADE)))
data class Transactions(
    val expenseTitle: String,
    val collectionId: Long,
    val comments: String,
    val amount: Float,
    val date: Long,
    val type: Int,
    var completed: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor(id: Long, expenseTitle: String, collectionId: Long, comments: String, amount: Float, date: Long, type: Int, completed: Boolean):
            this(expenseTitle, collectionId, comments, amount, date, type, completed) {
        this.id = id
    }
}