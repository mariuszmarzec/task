package com.marzec.usecase

import com.marzec.TestSchedulersRule
import com.marzec.createCurrencyData
import com.marzec.repository.CountriesRepository
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestSchedulersRule::class)
class LoadCurrenciesDataUseCaseTest {

    val repo = mock<CountriesRepository>()
    val useCase = LoadCurrenciesDataUseCase(repo)

    @BeforeEach
    fun setUp() {
        whenever(repo.getCountriesData()).thenReturn(Single.just(listOf(
                createCurrencyData("PLN"),
                createCurrencyData("USD")
        )))
    }

    @Test
    fun get_LoadingTwoTimes_DataLoadedFromCacheForSecondLoad() {
        useCase.get()
                .test()
                .assertNoErrors()
                .assertValue { it.get("PLN") != null && it.get("USD") != null }
        verify(repo, times(1)).getCountriesData()

        useCase.get()
                .test()
                .assertNoErrors()
                .assertValue { it.get("PLN") != null && it.get("USD") != null }
        verifyNoMoreInteractions(repo)
    }
}