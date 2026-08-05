package br.com.usinasantafe.cvf.utils

import kotlin.reflect.KProperty0

fun <T> KProperty0<T?>.required(): T =
    get() ?: throw NullPointerException("$name is required")

fun <T> T?.required(name: String): T =
    this ?: throw NullPointerException("$name is required")