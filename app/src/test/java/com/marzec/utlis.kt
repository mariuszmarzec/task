package com.marzec

import com.marzec.api.model.CountryResource
import com.marzec.api.model.CurrencyResource
import com.marzec.api.model.RatesResource
import com.marzec.extensions.emptyString
import com.marzec.model.CurrencyData
import com.marzec.model.Rate
import com.marzec.model.RateViewItem
import com.marzec.model.Rates
import java.math.BigDecimal

fun createRatesResource(base: String? = null, rates: Map<String?, Double?>? = null): RatesResource {
    return RatesResource(base, rates)
}

fun createRate(code: String = "EUR", value: BigDecimal = BigDecimal("1")): Rate {
    return Rate(code, value)
}

fun createRates(base: Rate = createRate(), currencies: List<Rate> = listOf(createRate())): Rates {
    return Rates(base, currencies)
}

fun createRateViewItem(
        code: String = "EUR",
        value: String = "1",
        editable: Boolean = false,
        currencyData: CurrencyData? = null
): RateViewItem {
    return RateViewItem(code, value, editable, currencyData)
}

fun createCountryResource(
        name: String? = null,
        alpha2Code: String? = null,
        currencies: List<CurrencyResource?>? = null
) =
        CountryResource(name, alpha2Code, currencies)

fun createCurrencyResource(code: String? = null, name: String? = null, symbol: String? = null) =
        CurrencyResource(code, name, symbol)

fun createCurrencyData(
        code: String = emptyString(),
        name: String = emptyString(),
        symbol: String = emptyString(),
        flagImg: String = emptyString()
) = CurrencyData(code, name, symbol, flagImg)