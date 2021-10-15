package com.example.stepapp.utils

import kotlin.math.*

fun kotlin.Int.length() = when(this) {
    0 -> 1
    else -> log10(abs(toDouble())).toInt() + 1
}