package com.example.expenses.repository.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(
    ForeignKey(entity = Expense::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("expenseId"),
    onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = TransactionCollection::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("collectionId"),
        onDelete = ForeignKey.CASCADE)))
data class Transactions(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val expenseId: Int,
    val collectionId: Int,
    val comments: String,
    val amount: Float,
    val date: Long,
    val type: Int,
    var completed: Boolean
)