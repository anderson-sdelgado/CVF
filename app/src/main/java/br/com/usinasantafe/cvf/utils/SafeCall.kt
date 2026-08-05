package br.com.usinasantafe.cvf.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch

suspend fun <T> call (
    context: String,
    block: suspend () -> T
): Result<T> {
    return runCatching {
        block()
    }.fold(
        onSuccess = { Result.success(it) },
        onFailure = { resultFailure(context = context, cause = it) }
    )
}

suspend fun <T> result (
    context: String,
    block: suspend () -> T
): Result<T> {
    return try {
        Result.success(block())
    } catch (e: Exception) {
        resultFailure(
            context = context,
            cause = e
        )
    }
}

suspend fun FlowCollector<UiStatusStateUpdate>.flowCall(
    context: String,
    block: suspend () -> Unit
) {
    try {
        block()
    } catch (e: Exception) {
        val failure = failure(context, e)
        emitFailure(failure)
    }
}

fun <T> callFlow(
    context: String,
    block: () -> Flow<T>
): Flow<T> =
    block().catch { e ->
        throw resultFailure(context, e).exceptionOrNull() ?: e
    }

suspend fun <T> tryCatch (
    context: String,
    block: suspend () -> T
): T {
    try {
        return block()
    } catch (e: Exception) {
        throw Exception(context, e)
    }
}
