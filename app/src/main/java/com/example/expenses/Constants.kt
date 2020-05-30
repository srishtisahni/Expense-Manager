package com.example.expenses

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Constants {
    companion object{
        val INCOME = 1
        val EXPENSE = 2
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val numberFormat: NumberFormat =  NumberFormat.getCurrencyInstance()
        init {
            numberFormat.currency = Currency.getInstance("INR")
        }

        fun getMonth(month: Int): String {
            return when(month) {
                1 -> "January"
                2 -> "February"
                3 -> "March"
                4 -> "April"
                5 -> "May"
                6 -> "June"
                7 -> "July"
                8 -> "August"
                9 -> "September"
                10 -> "October"
                11 -> "November"
                else -> "December"
            }
        }
    }
}