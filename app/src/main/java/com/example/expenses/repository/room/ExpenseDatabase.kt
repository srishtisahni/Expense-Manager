package com.example.expenses.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.expenses.repository.data.TransactionCollection
import com.example.expenses.repository.data.Expense
import com.example.expenses.repository.data.Transactions

@Database(entities = arrayOf(TransactionCollection::class, Expense::class, Transactions::class),
            exportSchema = false,
            version = 1)
abstract class ExpenseDatabase : RoomDatabase() {
    companion object{
        private val DB_NAME = "expense_db"
        private lateinit var INSTANCE: ExpenseDatabase

        fun getInstance(context: Context): ExpenseDatabase {
            return if(this::INSTANCE.isInitialized)
                INSTANCE
            else {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    ExpenseDatabase::class.java,
                    DB_NAME).fallbackToDestructiveMigration()
                    .build()
                INSTANCE
            }
        }
    }

    abstract fun getDao(): ExpenseDao
}