package com.marzec.usecase

import com.marzec.TestSchedulersRule
import com.marzec.createCurrencyData
import com.marzec.repository.CountriesRepository
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.net.UnknownHostException

@ExtendWith(TestSchedulersRule::class)
class LoadCurrenciesDataUseCaseTest {

    val repo = mock<CountriesRepository>()
    val useCase = LoadCurrenciesDataUseCase(repo)

    @Test
    fun get_LoadingTwoTimes_DataLoadedFromCacheForSecondLoad() {
        whenever(repo.getCountriesData()).thenReturn(Single.just(listOf(
                createCurrencyData("PLN"),
                createCurrencyData("USD")
        )))

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

    @Test
    fun get_FirstLoadingFailed_SecondLoadingSucceed() {
        whenever(repo.getCountriesData()).thenReturn(Single.error(UnknownHostException()))

        useCase.get()
                .test()
                .assertError { it is UnknownHostException }
        verify(repo, times(1)).getCountriesData()

        whenever(repo.getCountriesData()).thenReturn(Single.just(listOf(
                createCurrencyData("PLN"),
                createCurrencyData("USD")
        )))

        useCase.get()
                .test()
                .assertNoErrors()
                .assertValue { it.get("PLN") != null && it.get("USD") != null }
        verify(repo, times(2)).getCountriesData()
    }
}