package com.marzec.model

import java.math.BigDecimal

data class RateViewItem(
        val code: String,
        val value: String,
        val editable: Boolean
) {
    fun toRate(): Rate {
        return Rate(code, BigDecimal(value))
    }
}