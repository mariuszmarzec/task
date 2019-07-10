package com.marzec.model

import com.marzec.extensions.toBigDecimal
import java.math.BigDecimal

data class RateViewItem(
        val code: String,
        val value: String,
        val editable: Boolean,
        val info: CurrencyData?
) {
    fun toRate(): Rate {
        return Rate(code, value.toBigDecimal(BigDecimal("0")))
    }
}