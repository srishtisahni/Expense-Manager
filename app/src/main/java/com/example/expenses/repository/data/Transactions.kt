package com.example.expenses.repository.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = TransactionCollection::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("collectionId"),
    onDelete = ForeignKey.CASCADE)]
)
data class Transactions(
    val expenseTitle: String,
    var collectionId: Long,
    val comments: String,
    val amount: Float,
    var date: Long,
    val type: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor(id: Long, expenseTitle: String, collectionId: Long, comments: String, amount: Float, date: Long, type: Int):
            this(expenseTitle, collectionId, comments, amount, date, type) {
        this.id = id
    }

    constructor(reminder: Reminders, collectionId: Long) : this(reminder.expenseTitle, collectionId, reminder.comments, reminder.amount, reminder.date, reminder.type)
}