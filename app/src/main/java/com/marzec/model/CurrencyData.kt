package com.marzec.model

data class CurrencyData(
        val code: String,
        val name: String,
        val symbol: String,
        val flagImg: String
)

class CurrencyDataStorage(
        private val currencies: Map<String, CurrencyData>
) {
    fun get(code: String) = currencies[code]
}