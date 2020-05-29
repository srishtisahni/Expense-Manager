package com.example.expenses.repository.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val defAmount: Float
)