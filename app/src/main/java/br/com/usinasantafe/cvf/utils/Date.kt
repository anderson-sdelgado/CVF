package br.com.usinasantafe.cvf.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.text.format

fun dateOneWeekAgo(): Date {
    val c: Calendar = Calendar.getInstance()
    c.time = Date()
    c.add(Calendar.DATE, -7)
    return c.time
}

fun dateOneMonthAgo(): Date {
    val c: Calendar = Calendar.getInstance()
    c.time = Date()
    c.add(Calendar.MONTH, -1)
    return c.time
}

fun String.stringToDate(): Date {
    val formatter = SimpleDateFormat(
        "dd/MM/yyyy HH:mm:ss",
        Locale.getDefault()
    )

    return formatter.parse(this)
        ?: throw IllegalArgumentException("Invalid Date: $this")
}

fun Date.dateToString(): String {
    val formatter = SimpleDateFormat(
        "dd/MM/yyyy HH:mm",
        Locale.forLanguageTag("pt-BR")
    )

    return formatter.format(this)
        ?: throw IllegalArgumentException("Invalid Date: $this")
}
