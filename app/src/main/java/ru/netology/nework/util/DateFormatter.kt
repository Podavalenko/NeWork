package ru.netology.nework.util


import java.text.SimpleDateFormat
import java.text.DateFormat
import java.util.*

object DateFormatter {

    fun formatDateToDateTimeString(date: Date): String {
        return SimpleDateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT)
            .format(date)
    }

}