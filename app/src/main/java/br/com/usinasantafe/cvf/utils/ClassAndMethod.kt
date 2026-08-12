package br.com.usinasantafe.cvf.utils

import kotlin.text.contains
import kotlin.text.endsWith
import kotlin.text.substringAfter
import kotlin.text.substringAfterLast
import kotlin.text.substringBefore

fun getClassAndMethod(): String {
    val pkg = "br.com.usinasantafe"
    val stack = Throwable().stackTrace

    val calls = stack
        .filter {
            it.className.contains(pkg)
        }
        .map { el ->
            val clsFull = el.className.substringAfterLast('.')
            val realMethod = clsFull.substringAfter('$', "").substringBefore('$')
            val cls = clsFull.substringBefore('$')
            val meth = realMethod.ifBlank { el.methodName }
            "$cls.$meth"
        }
        .distinct()
        .reversed()

    val listFinish = calls
        .filter {
            !it.endsWith("invokeSuspend") &&
                    !it.contains("access") &&
                    !it.contains("Test") &&
                    !it.contains("handleFailure") &&
                    !it.contains("DefaultImpls") &&
                    !it.contains("SafeCallKt") &&
                    !it.contains("AppError") &&
                    !it.contains("lambda") &&
                    !it.contains("Screen") &&
                    !it.endsWith("Failure") &&
                    !it.endsWith("getClassAndMethod") &&
                    !it.endsWith("default")
        }
        .map {
            it.replace(".invoke", "").substringBefore('-')
        }


    return listFinish.joinToString(" -> ").ifBlank { "Classe.MetodoDesconhecido" }
}
