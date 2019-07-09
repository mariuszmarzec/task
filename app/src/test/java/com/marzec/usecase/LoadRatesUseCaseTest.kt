package com.marzec.usecase

import com.marzec.TestSchedulersRule
import com.marzec.createRate
import com.marzec.createRates
import com.marzec.repository.ConverterRepository
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import io.reactivex.plugins.RxJavaPlugins



@ExtendWith(TestSchedulersRule::class)
class LoadRatesUseCaseTest {

    val baseCurrency = "EUR"
    val rates = createRates(currencies = listOf(createRate(), createRate("USD", BigDecimal("1.15"))))

    lateinit var repo: ConverterRepository
    lateinit var useCase: LoadRatesUseCase

    @BeforeEach
    fun setUp() {
        repo = mock()
        useCase = LoadRatesUseCase(repo)
    }

    @Test
    fun get_LoadingDataPerSecond_Success() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        whenever(repo.getRates(baseCurrency)).thenReturn(Single.just(rates))

        val testSubscriber = useCase.get()
                .subscribeOn(testScheduler)
                .observeOn(testScheduler)
                .test()
        testSubscriber.assertNoValues()

        useCase.setArg(createRate(code = baseCurrency))
        testScheduler.triggerActions()
        testSubscriber.assertValue(rates)

        testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS)
        testSubscriber.assertValueCount(1)

        testScheduler.advanceTimeTo(1, TimeUnit.SECONDS)
        testSubscriber.assertValueAt(1, rates)
        verify(repo, times(2)).getRates(baseCurrency)
        verifyNoMoreInteractions(repo)
    }

    @Test
    fun get_ChangingBaseRate_Success() {
        val testScheduler = TestScheduler()
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        val dollarCode = "USD"
        val ratesForDollar = createRates(createRate(dollarCode), listOf(createRate(dollarCode), createRate()))

        whenever(repo.getRates(baseCurrency)).thenReturn(Single.just(rates))
        whenever(repo.getRates(dollarCode)).thenReturn(Single.just(ratesForDollar))

        val testSubscriber = useCase.get()
                .subscribeOn(testScheduler)
                .observeOn(testScheduler)
                .test()
        testSubscriber.assertNoValues()

        useCase.setArg(createRate(code = baseCurrency))
        testScheduler.triggerActions()
        testSubscriber.assertValue(rates)

        testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS)
        testSubscriber.assertValueCount(1)

        useCase.setArg(createRate(dollarCode))
        testScheduler.triggerActions()
        testSubscriber.assertValueAt(1, ratesForDollar)
        verify(repo, times(1)).getRates(baseCurrency)
        verify(repo, times(1)).getRates(dollarCode)
        verifyNoMoreInteractions(repo)
    }
}