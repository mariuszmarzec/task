package com.marzec.api.model

data class RatesResource(
        val base: String?,
        val rates: Map<String?, Double?>?
)