package br.com.usinasantafe.cvf.utils

fun updatePercentage(pos: Float, count: Float, sizeAll: Float): Float {
    return percentage(pos + ((count - 1) * 3), sizeAll)
}

fun percentage(count: Float, size: Float): Float {
    return (count / size)
}

fun sizeUpdate(qtdTable: Float = 1f): Float {
    return (qtdTable * 3) + 1f
}