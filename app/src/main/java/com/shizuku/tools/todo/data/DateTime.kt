package com.shizuku.tools.todo.data

import java.text.DateFormat

import java.util.*

class DateTime {
    var cal: Calendar = Calendar.getInstance()

    constructor(millis: Long) {
        cal.timeInMillis = millis
    }

    constructor()

    fun num(): Long {
        return cal.timeInMillis
    }

    fun dateString(): String {
        val date = cal.time
        val f = DateFormat.getDateInstance()
        return f.format(date)
    }

    fun timeString(): String {
        var min = cal.get(Calendar.MINUTE).toString()
        if (min.length <= 1) {
            min = "0$min"
        }
        return cal.get(Calendar.HOUR_OF_DAY).toString() + ":" + min
    }

    fun string(): String {
        return dateString() + " " + timeString()
    }

    fun set(year: Int, month: Int, day: Int) {
        cal.set(year, month, day)
    }

    fun set(hour: Int, minute: Int) {
        cal.set(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH),
            hour,
            minute,
            0
        )
    }

    companion object {
        fun todayStart(): Long {
            val c: Calendar = Calendar.getInstance()
            c.set(
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH),
                0,
                0,
                0
            )
            return c.timeInMillis
        }

        fun todayEnd(): Long {
            val c: Calendar = Calendar.getInstance()
            c.set(
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH),
                23,
                59,
                59
            )
            return c.timeInMillis
        }

        fun current(): Long {
            val c: Calendar = Calendar.getInstance()

            return c.timeInMillis
        }
    }

}
