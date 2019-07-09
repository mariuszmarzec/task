package com.marzec.repository

import com.marzec.TestSchedulersRule
import com.marzec.api.ConverterApi
import com.marzec.createRate
import com.marzec.createRatesResource
import com.marzec.model.Rates
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal

@ExtendWith(TestSchedulersRule::class)
class ConverterRepositoryTest {

    lateinit var api: ConverterApi
    lateinit var repository: ConverterRepository

    @BeforeEach
    fun setUp() {
        api = mock()
        repository = ConverterRepository(api)
    }

    @Test
    fun getRates_CorruptedData_Success() {
        val baseCurrency = "EUR"
        whenever(api.getCurrencies(baseCurrency)).thenReturn(
                Single.just(createRatesResource(
                        baseCurrency,
                        mapOf(
                                Pair(null, null),
                                Pair("USD", 1.15),
                                Pair("", 12.15),
                                Pair("PLN", 4.3),
                                Pair("COL", null)
                        )
                )))
        repository.getRates(baseCurrency)
                .test()
                .awaitCount(1)
                .assertValue(
                        Rates(createRate(baseCurrency, BigDecimal("1")), listOf(
                                createRate(baseCurrency, BigDecimal("1")),
                                createRate("USD", BigDecimal("1.15")),
                                createRate("PLN", BigDecimal("4.3"))
                        )))

        verify(api).getCurrencies(eq(baseCurrency))
        verifyNoMoreInteractions(api)
    }
}