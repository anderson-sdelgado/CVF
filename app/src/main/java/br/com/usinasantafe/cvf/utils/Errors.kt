package br.com.usinasantafe.cvf.utils

import br.com.usinasantafe.cvf.lib.Errors
import timber.log.Timber

const val ERROR_STRING_TO_LONG = "stringToLong"


fun resultFailure(
    context: String,
    cause: Throwable
): Result<Nothing>  {
    val technicalCause = if (cause is AppError) cause.cause else cause
    return resultFailure(
        context = context,
        message = cause.message,
        cause = technicalCause
    )
}

fun resultFailure(
    context: String,
    cause: Exception
): Result<Nothing>  {
    return resultFailure(
        context = context,
        message = "-",
        cause = cause
    )
}

fun resultFailure(
    context: String,
    message: String?,
    cause: Throwable? = null
): Result<Nothing>  {
    return Result.failure(
        AppError(
            context = context,
            message = message,
            cause = cause
        )
    )
}

class AppError(
    context: String,
    message: String?,
    cause: Throwable? = null
) : Exception(removeRepeatedCalls("$context${if (message == null) "" else if (message == "-") "" else " -> $message"}"), cause)

fun failure(classAndMethod: String, error: Throwable) : String {
    val cause = if(error.cause == null) "" else  " -> ${error.cause.toString()}"
    return removeRepeatedCalls("$classAndMethod -> ${error.message}$cause")
}

fun removeRepeatedCalls(path: String): String {
    return path
        .split(" -> ")
        .asReversed()
        .distinct()
        .asReversed()
        .joinToString(" -> ")
}


fun <T> handleFailure(
    failure: String,
    classAndMethod: String,
    block: (String) -> T,
) {
    val fail = "$classAndMethod -> $failure"
    Timber.e(fail)
    block(fail)
}

fun <T> handleFailure(
    error: Throwable,
    classAndMethod: String,
    block: (String) -> T,
) {
    val cause = if(error.cause != null) " -> ${error.cause.toString()}" else ""
    val failure = "${error.message}$cause"
    handleFailure(failure, classAndMethod, block)
}

fun Result<*>.onFailureHandled(
    classAndMethod: String,
    block: (String) -> Unit
) {
    onFailure { error ->
        handleFailure(error, classAndMethod, block)
    }
}


fun failure(error: Errors): String {
    return when(error){
        else -> error.toString()
    }
}


fun handleFailure(
    failure: String,
    classAndMethod: String,
) {
    val fail = "$classAndMethod -> $failure"
    Timber.e(fail)
}

fun handleFailure(
    error: Throwable,
    classAndMethod: String,
) {
    val cause = if(error.cause != null) " -> ${error.cause.toString()}" else ""
    val failure = "${error.message}$cause"
    handleFailure(failure, classAndMethod)
}