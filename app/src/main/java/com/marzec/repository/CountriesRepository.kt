package com.marzec.repository

import com.marzec.api.CountriesApi
import com.marzec.model.CurrencyData
import java.util.*
import javax.inject.Inject

class CountriesRepository @Inject constructor(
        private val countriesApi: CountriesApi
) {
    private val iconUrlPattern = "https://www.countryflags.io/%s/shiny/64.png"

    fun getCountriesData() = countriesApi.getAllCountries().map {
        it.mapNotNull { country ->
            country.currencies?.mapNotNull { currency ->
                if (!currency?.code.isNullOrEmpty()) {
                    CurrencyData(
                            currency?.code.orEmpty(),
                            currency?.name.orEmpty(),
                            currency?.symbol.orEmpty(),
                            country.alpha2Code?.let { String.format(Locale.getDefault(), iconUrlPattern, it) }.orEmpty()
                    )
                } else {
                    null
                }
            }.orEmpty().firstOrNull()
        }
    }
}