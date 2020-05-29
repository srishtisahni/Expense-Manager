package com.example.expenses.repository.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TransactionCollection(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val month: Int,
    val year: Int
)