package br.com.usinasantafe.cvf.utils

import br.com.usinasantafe.cvf.lib.Errors
import br.com.usinasantafe.cvf.lib.LevelUpdate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import timber.log.Timber

data class UiStatusStateUpdate(
    val flagAccess: Boolean = false,
    val flagDialog: Boolean = false,
    val flagFailure: Boolean = false,
    val errors: Errors = Errors.FIELD_EMPTY,
    val failure: String = "",
    val flagProgress: Boolean = false,
    val currentProgress: Float = 0.0f,
    val levelUpdate: LevelUpdate? = null,
    val tableUpdate: String = "",
)

suspend fun Flow<UiStatusStateUpdate>.collectUpdateStep(
    classAndMethod: String,
    currentStatus: UiStatusStateUpdate,
    flagProgressOnFailure: Boolean = false,
    emitState: suspend (UiStatusStateUpdate) -> Unit
): Boolean {

    var ok = true

    collect { result ->
        val newStatus = result.toUiStatus(classAndMethod, currentStatus, flagProgressOnFailure)
        emitState(newStatus)

        if (newStatus.flagFailure) {
            ok = false
            return@collect
        }
    }

    return ok
}

fun UiStatusStateUpdate.toUiStatus(
    classAndMethod: String,
    current: UiStatusStateUpdate,
    flagProgressOnFailure: Boolean = false,
): UiStatusStateUpdate {

    if (flagFailure) {
        return current.withFailure(
            classAndMethod = classAndMethod,
            message = failure,
            errors = errors,
            flagProgress = flagProgressOnFailure
        )
    }

    return current.copy(
        flagDialog = flagDialog,
        flagFailure = false,
        errors = errors,
        failure = failure,
        flagProgress = flagProgress,
        currentProgress = currentProgress,
        levelUpdate = levelUpdate,
        tableUpdate = tableUpdate
    )
}

fun UiStatusStateUpdate.withFailure(
    classAndMethod: String,
    message: String,
    errors: Errors = Errors.EXCEPTION,
    flagProgress: Boolean = false
): UiStatusStateUpdate {

    val failMsg = removeRepeatedCalls("$classAndMethod -> $message")
    Timber.e(failMsg)

    return copy(
        flagDialog = true,
        flagFailure = true,
        failure = failMsg,
        errors = errors,
        flagProgress = flagProgress,
        currentProgress = 1f,
        levelUpdate = null,
        tableUpdate = ""
    )
}

fun UiStatusStateUpdate.withFailure(
    classAndMethod: String,
    throwable: Throwable,
    errors: Errors = Errors.EXCEPTION,
    flagProgress: Boolean = false
): UiStatusStateUpdate {
    val msg = "${throwable.message} -> ${throwable.cause}"
    return withFailure(classAndMethod, msg, errors, flagProgress)
}

suspend fun FlowCollector<UiStatusStateUpdate>.emitProgress(
    count: Float,
    sizeAll: Float,
    level: LevelUpdate,
    table: String = "",
    flagProgress: Boolean = true
) {
    val step = when(level){
        LevelUpdate.RECOVERY, LevelUpdate.GET_TOKEN -> 1f
        LevelUpdate.CLEAN, LevelUpdate.SAVE_TOKEN -> 2f
        LevelUpdate.SAVE, LevelUpdate.FINISH_UPDATE_INITIAL -> 3f
        else -> 0f
    }
    emit(
        UiStatusStateUpdate(
            flagProgress = flagProgress,
            currentProgress = updatePercentage(step, count, sizeAll),
            tableUpdate = table,
            levelUpdate = level
        )
    )
}

suspend fun FlowCollector<UiStatusStateUpdate>.emitFailure(
    failure: String,
    errors: Errors
) {
    emit(
        UiStatusStateUpdate(
            flagProgress = false,
            errors = errors,
            flagDialog = true,
            flagFailure = true,
            failure = failure,
            currentProgress = 1f,
            levelUpdate = null
        )
    )
}

fun <STATE> executeUpdateSteps(
    steps: List<Flow<UiStatusStateUpdate>>,
    getState: () -> STATE,
    getStatus: (STATE) -> UiStatusStateUpdate,
    copyStateWithStatus: (STATE, UiStatusStateUpdate) -> STATE,
    classAndMethod: String,
    flagUpdateFinish: Boolean = true,
    flagProgressOnFailure: Boolean = false,
    flagDialog: Boolean = true
): Flow<STATE> = flow {

    for (step in steps) {
        val ok = step.collectUpdateStep(
            classAndMethod = classAndMethod,
            currentStatus = getStatus(getState()),
            flagProgressOnFailure = flagProgressOnFailure
        ) { status ->
            val newState = copyStateWithStatus(getState(), status)
            emit(newState)
        }
        if (!ok) return@flow
    }

    if (flagUpdateFinish) {
        val finalStatus = getStatus(getState()).copy(
            flagDialog = flagDialog,
            flagProgress = false,
            flagFailure = false,
            levelUpdate = LevelUpdate.FINISH_UPDATE_COMPLETED,
            currentProgress = 1f,
        )
        val finalState = copyStateWithStatus(getState(), finalStatus)
        emit(finalState)
    }


}

interface UiStateWithStatusUpdate<T : UiStateWithStatusUpdate<T>> {
    val status: UiStatusStateUpdate

    fun copyWithStatus(status: UiStatusStateUpdate): T

    fun withFailure(
        classAndMethod: String,
        throwable: Throwable,
        errors: Errors = Errors.EXCEPTION,
        flagProgress: Boolean = false
    ): T =
        copyWithStatus(
            status.withFailure(
                classAndMethod,
                throwable,
                errors,
                flagProgress
            )
        )

    fun withAccess(check: Boolean): T =
        copyWithStatus(
            status.copy(
                flagAccess = check,
                flagDialog = !check,
                flagFailure = !check,
                errors = Errors.INVALID
            )
        )

    fun withFinishUpdate(): T =
        copyWithStatus(
            status.copy(
                tableUpdate = "",
                flagDialog = true,
                flagProgress = false,
                flagFailure = false,
                levelUpdate = LevelUpdate.FINISH_UPDATE_COMPLETED,
                currentProgress = 1f
            )
        )

}

fun <T : UiStateWithStatusUpdate<T>> UiStateWithStatusUpdate<T>.withFailure(
    classAndMethod: String,
    error: Errors = Errors.INVALID,
    flagProgress: Boolean = false,
    failure: String = ""
): T =
    copyWithStatus(
        status.withFailure(
            classAndMethod = classAndMethod,
            message = failure.ifEmpty { failure(error) },
            errors = error,
            flagProgress = flagProgress
        )
    )

fun <T : UiStateWithStatusUpdate<T>> Result<*>.onSuccessUpdateAccess(
    updateState: ((T.() -> T)) -> Unit
): Result<*> =
    onSuccess {
        updateState { withAccess(true) }
    }

fun <T : UiStateWithStatusUpdate<T>> Result<*>.onSuccessUpdateFinish(
    updateState: ((T.() -> T)) -> Unit
): Result<*> =
    onSuccess {
        updateState { withFinishUpdate() }
    }

fun <T : UiStateWithStatusUpdate<T>> Result<Boolean>.onSuccessUpdateCheckAccess(
    updateState: ((T.() -> T)) -> Unit
): Result<Boolean> =
    onSuccess { check ->
        updateState { withAccess(check) }
    }


fun <T : UiStateWithStatusUpdate<T>> Result<*>.onFailureUpdate(
    classAndMethod: String,
    updateState: ((T.() -> T)) -> Unit,
    errors: Errors = Errors.EXCEPTION,
    flagProgress: Boolean = false
): Result<*> =
    onFailure { failure ->
        updateState {
            withFailure(classAndMethod, failure, errors, flagProgress)
        }
    }


suspend inline fun <T : UiStateWithStatusUpdate<T>> Result<*>.onFailureEmit(
    collector: FlowCollector<T>,
    currentState: T,
    classAndMethod: String,
    errorType: Errors = Errors.TOKEN
) {
    this.onFailure { throwable ->
        val newState = currentState.withFailure(
            classAndMethod = classAndMethod,
            throwable = throwable,
            errors = errorType,
            flagProgress = false
        )
        collector.emit(newState)
    }
}