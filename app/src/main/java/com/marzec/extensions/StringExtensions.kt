package com.marzec.extensions

import java.lang.NumberFormatException
import java.math.BigDecimal
import java.text.DecimalFormatSymbols

fun emptyString() = ""

fun String.toBigDecimal(): BigDecimal? {
    return try {
        if (this == emptyString()) return BigDecimal(0)
        val decimalSeparator = DecimalFormatSymbols.getInstance().decimalSeparator
        return if (decimalSeparator == ',') {
            BigDecimal(this.replace(decimalSeparator.toString(), "."))
        } else {
            BigDecimal(this)
        }
    } catch (e: NumberFormatException) {
        null
    }
}

fun String.toBigDecimal(inCaseOfError: BigDecimal): BigDecimal {
    return toBigDecimal() ?: inCaseOfError
}