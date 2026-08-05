package br.com.usinasantafe.cvf.utils

import java.util.Calendar
import java.util.Date

fun dateOneWeekAgo(): Date {
    val c: Calendar = Calendar.getInstance()
    c.time = Date()
    c.add(Calendar.DATE, -7)
    return c.time
}
