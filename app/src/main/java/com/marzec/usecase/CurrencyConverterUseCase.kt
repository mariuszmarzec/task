package com.marzec.usecase

import com.marzec.base.BaseUseCase
import com.marzec.model.Rate
import com.marzec.model.Rates
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

class CurrencyConverterUseCase @Inject constructor(
        private val loadRatesUseCase: LoadRatesUseCase
) : BaseUseCase<Rate, List<Rate>> {

    private val currentBase = BehaviorSubject.create<Rate>()
    private var initValue: AtomicReference<List<Rate>> = AtomicReference(emptyList())

    override fun setArg(arg: Rate) {
        currentBase.onNext(arg)
        loadRatesUseCase.setArg(arg)
    }

    override fun get(): Flowable<List<Rate>> {
        return Flowable.combineLatest(
                currentBase.toFlowable(BackpressureStrategy.LATEST),
                loadRatesUseCase.get(), BiFunction { base: Rate, rates: Rates ->
            Pair(base, rates)
        }).scan(initValue.get(), { previous, (newBase, rates) ->
            val newBaseRatioToOldBase = rates.currencies.firstOrNull {
                newBase.code == it.code
            }
            rates.currencies
                    .calcRates(newBase, rates.base, newBaseRatioToOldBase)
                    .sortBasedOnPreviousOrder(newBase, previous)
        }).doOnNext { initValue.set(it) }
    }

    private fun List<Rate>.calcRates(newBase: Rate, loadedBase: Rate, newBaseRatioToLoadedBase: Rate?) =
            if (loadedBase.code != newBase.code && newBaseRatioToLoadedBase != null) {
                map { rate ->
                    if (rate.code == newBase.code) {
                        newBase.copy()
                    } else {
                        rate.copy(value = (newBase.value / newBaseRatioToLoadedBase.value * rate.value))
                    }
                }
            } else {
                map { it.copy(value = (newBase.value * it.value)) }
            }

    private fun List<Rate>.sortBasedOnPreviousOrder(newBase: Rate, previous: List<Rate>): List<Rate> {
        val rateOrder = previous
                .ifEmpty { listOf(newBase) }
                .mapIndexed { index, rate ->
                    rate.code to index
                }.toMap()
        return sortedBy {
            if (it.code == newBase.code) {
                Int.MIN_VALUE
            } else {
                rateOrder[it.code] ?: Int.MAX_VALUE
            }
        }
    }
}