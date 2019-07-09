package com.marzec.usecase

import com.marzec.TestSchedulersRule
import com.marzec.createRate
import com.marzec.createRates
import com.marzec.model.Rate
import com.marzec.model.Rates
import com.nhaarman.mockitokotlin2.*
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal

@ExtendWith(TestSchedulersRule::class)
internal class CurrencyConverterUseCaseTest {

    val baseCurrency = "EUR"
    lateinit var loadUseCase: LoadRatesUseCase
    lateinit var useCase: CurrencyConverterUseCase
    val subject = PublishSubject.create<Rates>()


    @BeforeEach
    fun setUp() {
        loadUseCase = mock()
        useCase = CurrencyConverterUseCase(loadUseCase)
        whenever(loadUseCase.get()).thenReturn(subject.toFlowable(BackpressureStrategy.BUFFER))
    }

    @Test
    fun get_InitialLoad_Success() {
        useCase.setArg(Rate(baseCurrency, BigDecimal("1")))
        val testSubscriber = useCase.get().test()

        subject.onNext(createRates(
                createRate(),
                listOf(createRate(), createRate("USD", BigDecimal(2)))
        ))

        testSubscriber.assertValue(listOf(createRate(), createRate("USD", BigDecimal(2))))
        verify(loadUseCase).get()
        verify(loadUseCase).setArg(createRate())
        verifyNoMoreInteractions(loadUseCase)
    }

    @Test
    fun get_ChangeBaseCurrency_NewBaseReorderAsFirst() {
        useCase.setArg(Rate(baseCurrency, BigDecimal("1")))
        val testSubscriber = useCase.get().test()

        subject.onNext(createRates(
                createRate(),
                listOf(createRate(), createRate("USD", BigDecimal(2)))
        ))

        subject.onNext(createRates(
                createRate(),
                listOf(createRate(), createRate("USD", BigDecimal(2)))
        ))

        testSubscriber.assertValue(listOf(createRate(), createRate("USD", BigDecimal(2))))
    }
}