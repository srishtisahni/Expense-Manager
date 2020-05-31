package com.example.expenses.repository.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Reminders(
    val expenseTitle: String,
    val comments: String,
    val amount: Float,
    var date: Long,
    val type: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor(id: Long, expenseTitle: String, comments: String, amount: Float, date: Long, type: Int):
            this(expenseTitle, comments, amount, date, type) {
        this.id = id
    }
}