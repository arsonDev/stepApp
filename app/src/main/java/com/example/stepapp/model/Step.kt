package com.example.stepapp.model

import com.example.stepapp.utils.length
import kotlin.math.ceil
import kotlin.math.pow

class Step {
    private var _count = 0

    var count
        get() = _count
        set(value) {
            _count += value
            calculateProgress()
        }

    val progress
        get() = calculateProgress()

    private fun calculateProgress() : Int {
        var result = 0
        var tempSteps = _count
        if (tempSteps > 10000) {
            tempSteps -= ("$tempSteps"[0].digitToInt() * 10.0.pow(tempSteps.length() - 1.0)).toInt()
        }
        result = ceil(tempSteps.toDouble() / 100).toInt()

        if (result > 100)
            result = 0;

        return result
    }
}