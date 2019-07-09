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
    val dollarCode = "USD"
    val zlotyCode = "PLN"
    val euroRate = createRate()
    val dollarRate = createRate(dollarCode, BigDecimal("2"))
    val zlotyRate = createRate(zlotyCode, BigDecimal("3"))

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
        useCase.setArg(euroRate)
        val testSubscriber = useCase.get().test()
        testSubscriber.assertValue(emptyList())

        subject.onNext(createRates(
                euroRate,
                listOf(euroRate, dollarRate)
        ))

        testSubscriber.assertValueAt(1, listOf(euroRate, dollarRate))

        subject.onNext(createRates(
                euroRate,
                listOf(euroRate, dollarRate)
        ))

        testSubscriber.assertValueAt(2, listOf(euroRate, dollarRate))
        verify(loadUseCase).setArg(euroRate)
        verify(loadUseCase).get()
        verifyNoMoreInteractions(loadUseCase)
    }

    @Test
    fun get_ChangeBaseCurrency_NewBaseReorderAsFirst() {
        val testSubscriber = useCase.get().test()
        testSubscriber.assertValue(emptyList())
        useCase.setArg(euroRate)

        subject.onNext(createRates(
                euroRate,
                listOf(euroRate, dollarRate, zlotyRate)
        ))
        testSubscriber.assertValueAt(1, listOf(
                euroRate,
                dollarRate,
                zlotyRate
        ))

        useCase.setArg(dollarRate)
        testSubscriber.assertValueAt(2, listOf(
                dollarRate,
                euroRate,
                zlotyRate
        ))

        useCase.setArg(zlotyRate)
        testSubscriber.assertValueAt(3, listOf(
                zlotyRate,
                dollarRate,
                euroRate
        ))
    }

    @Test
    fun get_ChangeBaseCurrencyValue_RecalculateAllValues() {
        val testSubscriber = useCase.get().test()
        testSubscriber.assertValue(emptyList())
        useCase.setArg(Rate(baseCurrency, BigDecimal("1")))

        subject.onNext(createRates(
                euroRate,
                listOf(euroRate, dollarRate, zlotyRate)
        ))
        testSubscriber.assertValueAt(1, listOf(
                euroRate,
                dollarRate,
                zlotyRate
        ))

        useCase.setArg(euroRate.copy(value = BigDecimal.valueOf(2)))
        testSubscriber.assertValueAt(2, listOf(
                euroRate.copy(value = BigDecimal.valueOf(2)),
                dollarRate.copy(value = BigDecimal.valueOf(4)),
                zlotyRate.copy(value = BigDecimal.valueOf(6))
        ))

        subject.onNext(createRates(
                euroRate,
                listOf(
                        euroRate,
                        dollarRate.copy(value = BigDecimal("3")),
                        zlotyRate.copy(value = BigDecimal("4"))
                )
        ))

        useCase.setArg(euroRate.copy(value = BigDecimal.valueOf(2)))
        testSubscriber.assertValueAt(3, listOf(
                euroRate.copy(value = BigDecimal.valueOf(2)),
                dollarRate.copy(value = BigDecimal.valueOf(6)),
                zlotyRate.copy(value = BigDecimal.valueOf(8))
        ))
    }

    @Test
    fun get_SubscribeAgain_ReturnLatestValueBeforeStoppingSubscription() {
        val testSubscriber = useCase.get().test()
        testSubscriber.assertValue(emptyList())
        useCase.setArg(euroRate)

        subject.onNext(createRates(
                euroRate,
                listOf(euroRate, dollarRate, zlotyRate)
        ))
        testSubscriber.assertValueAt(1, listOf(
                euroRate,
                dollarRate,
                zlotyRate
        ))
        testSubscriber.dispose()

        useCase.get().test().assertValue(listOf(
                euroRate,
                dollarRate,
                zlotyRate
        ))
    }
}