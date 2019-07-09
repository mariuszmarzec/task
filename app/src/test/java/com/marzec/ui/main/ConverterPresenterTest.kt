package com.marzec.ui.main

import com.marzec.TestSchedulersRule
import com.marzec.createRate
import com.marzec.createRateViewItem
import com.marzec.model.Rate
import com.marzec.usecase.CurrencyConverterUseCase
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import net.bytebuddy.asm.Advice
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.lang.Exception
import java.math.BigDecimal
import java.util.*

@ExtendWith(TestSchedulersRule::class)
class ConverterPresenterTest {

    val view = mock<ConverterContract.View>()
    val useCase: CurrencyConverterUseCase = mock()
    val presenter = ConverterPresenter(useCase)

    val subject = PublishSubject.create<List<Rate>>()

    @BeforeEach
    fun setUp() {
        Locale.setDefault(Locale.US)
    }

    @Test
    fun load_LoadingData_showRates() {
        whenever(useCase.get()).thenReturn(subject.toFlowable(BackpressureStrategy.BUFFER))

        presenter.attach(view)
        presenter.load()

        subject.onNext(listOf(
                createRate(code = "USD", value = BigDecimal("1")),
                createRate(code = "EUR", value = BigDecimal("0.9"))
        ))

        verify(view).showRates(listOf(
                createRateViewItem(code = "USD", value = "1", editable = true),
                createRateViewItem(code = "EUR", value = "0.9", editable = false)
        ))
        verifyNoMoreInteractions(view)
    }

    @Test
    fun load_LoadingDataFailed_showError() {
        whenever(useCase.get()).thenReturn(Flowable.error(Exception()))

        presenter.attach(view)
        presenter.load()

        verify(view).showError()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun setBaseCurrency() {
        presenter.attach(view)
        presenter.setBaseCurrency(createRate(code = "USD", value = BigDecimal("1")))

        verify(useCase).setArg(createRate(code = "USD", value = BigDecimal("1")))
        verifyNoMoreInteractions(view)
    }
}