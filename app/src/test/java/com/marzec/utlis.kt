package com.marzec

import com.marzec.api.model.RatesResource
import com.marzec.model.Rate
import com.marzec.model.Rates
import java.math.BigDecimal

fun createRatesResource(base: String? = null, rates: Map<String?, Double?>? = null): RatesResource {
    return RatesResource(base, rates)
}

fun createRate(code: String = "EUR", value: BigDecimal = BigDecimal.valueOf(1)): Rate {
    return Rate(code, value)
}

fun createRates(base: Rate = createRate(), currencies: List<Rate> = listOf(createRate())): Rates {
    return Rates(base, currencies)
}