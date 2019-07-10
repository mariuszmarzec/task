package com.marzec.model

data class RateViewItem(
        val code: String,
        val value: String,
        val editable: Boolean,
        val info: CurrencyData?
)