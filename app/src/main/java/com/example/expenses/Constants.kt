package com.example.expenses

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Constants {
    companion object{
        var balance = 0f
        var salary = 0f
        var budget = 0f
        var savings = 0f

        val INCOME = 1
        val EXPENSE = 2
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val currencyFormat: NumberFormat =  NumberFormat.getCurrencyInstance()
        init {
            currencyFormat.currency = Currency.getInstance("INR")
        }

        fun getMonth(month: Int): String {
            return when(month) {
                0 -> "January"
                1 -> "February"
                2 -> "March"
                3 -> "April"
                4 -> "May"
                5 -> "June"
                6 -> "July"
                7 -> "August"
                8 -> "September"
                9 -> "October"
                10 -> "November"
                else -> "December"
            }
        }
    }
}