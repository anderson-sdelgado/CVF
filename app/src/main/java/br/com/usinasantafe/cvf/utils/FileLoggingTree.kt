package br.com.usinasantafe.cvf.utils

import android.content.Context
import android.util.Log
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FileLoggingTree(context: Context) : Timber.Tree() {

    private val logFile = File(context.filesDir, "app_log.txt")
    private val maxLines = 1000

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.ERROR || priority == Log.WARN) {
            val timeStamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val logMessage = "\n$timeStamp - $message"

            try {
                if (!logFile.exists()) {
                    logFile.createNewFile()
                }

                FileWriter(logFile, true).apply {
                    write(logMessage)
                    close()
                }

                // Limpa linhas antigas, mantendo apenas as mais novas
                trimOldLogs()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun trimOldLogs() {
        if (!logFile.exists()) return

        val lines = logFile.readLines()
        if (lines.size > maxLines) {
            val newContent = lines.takeLast(maxLines) // Mantém apenas as últimas 'maxLines'
            logFile.writeText(newContent.joinToString("\n")) // Sobrescreve o arquivo
        }
    }
}