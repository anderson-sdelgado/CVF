package br.com.usinasantafe.cvf.utils

import br.com.usinasantafe.cvf.lib.Errors
import timber.log.Timber

data class UiStatusState(
    val flagAccess: Boolean = false,
    val flagDialog: Boolean = false,
    val flagFailure: Boolean = false,
    val failure: String = "",
    val errors: Errors = Errors.EXCEPTION,
)

interface UiStateWithStatus<T : UiStateWithStatus<T>> {
    val status: UiStatusState

    fun copyWithStatus(status: UiStatusState): T

    fun withSuccess(): T =
        copyWithStatus(
            status.copy(
                flagAccess = true, flagDialog = false
            )
        )

    fun withFailure(
        classAndMethod: String,
        throwable: Throwable,
        errors: Errors = Errors.EXCEPTION
    ): T =
        copyWithStatus(
            status.withFailure(
                classAndMethod,
                throwable,
                errors
            )
        )

}

fun UiStatusState.withFailure(
    classAndMethod: String,
    throwable: Throwable,
    errors: Errors = Errors.EXCEPTION
): UiStatusState {
    val msg = "${throwable.message} -> ${throwable.cause}"
    return withFailure(classAndMethod, msg, errors)
}

fun UiStatusState.withFailure(
    classAndMethod: String,
    message: String,
    errors: Errors = Errors.EXCEPTION
): UiStatusState {

    val failMsg = "$classAndMethod -> $message"
    Timber.e(failMsg)

    return copy(
        flagAccess = false,
        flagDialog = true,
        flagFailure = true,
        failure = failMsg,
        errors = errors,
    )
}

fun <T : UiStateWithStatus<T>> Result<*>.onSuccessStateAccess(
    updateState: ((T.() -> T)) -> Unit
): Result<*> =
    onSuccess {
        updateState {
            withSuccess()
        }
    }

fun <T : UiStateWithStatus<T>> Result<*>.onFailureState(
    classAndMethod: String,
    updateState: ((T.() -> T)) -> Unit
): Result<*> =
    onFailure { failure ->
        updateState {
            withFailure(classAndMethod, failure)
        }
    }

fun <T : UiStateWithStatus<T>> UiStateWithStatus<T>.withFailure(
    classAndMethod: String,
    error: Errors = Errors.INVALID,
    failure: String = ""
): T =
    copyWithStatus(
        status.withFailure(
            classAndMethod = classAndMethod,
            message = failure.ifEmpty { failure(error) },
            errors = error,
        )
    )
