package br.com.usinasantafe.cav.utils

import androidx.compose.ui.test.junit4.ComposeTestRule
import java.util.Timer
import kotlin.concurrent.schedule

fun ComposeTestRule.waitUntilTimeout(
    timeoutMillis: Long = 2_000
) {
    AsyncTimer.start(timeoutMillis)
    this.waitUntil(
        condition = { AsyncTimer.expired },
        timeoutMillis = timeoutMillis + 1000
    )
}

object AsyncTimer {
    var expired = false
    fun start(delay: Long = 1000) {
        expired = false
        Timer().schedule(delay) {
            expired = true
        }
    }
}