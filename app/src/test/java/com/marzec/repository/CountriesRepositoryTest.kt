package com.marzec.repository

import com.marzec.TestSchedulersRule
import com.marzec.api.CountriesApi
import com.marzec.createCountryResource
import com.marzec.createCurrencyData
import com.marzec.createCurrencyResource
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestSchedulersRule::class)
internal class CountriesRepositoryTest {

    val api = mock<CountriesApi>()
    val repository = CountriesRepository(api)

    @Test
    fun getCountriesData_CorruptedData_SuccessLoad() {
        whenever(api.getAllCountries()).thenReturn(Single.just(listOf(
                createCountryResource("Poland", "pl", currencies = listOf(createCurrencyResource("PLN", "Polish zloty", "zł"))),
                createCountryResource("European Union", currencies = listOf(createCurrencyResource("EUR", "Euro"))),
                createCountryResource("None", currencies = listOf(createCurrencyResource(""))),
                createCountryResource(null, currencies = listOf(createCurrencyResource("USD")))
        )))
        repository.getCountriesData()
                .test()
                .assertValue(listOf(
                        createCurrencyData("PLN", "Polish zloty", "zł", "https://www.countryflags.io/pl/shiny/64.png"),
                        createCurrencyData("EUR", "Euro"),
                        createCurrencyData("USD")
                ))
    }
}