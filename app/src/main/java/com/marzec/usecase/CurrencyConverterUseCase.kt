package com.marzec.usecase

import com.marzec.base.BaseUseCase
import com.marzec.model.Rate
import com.marzec.model.Rates
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.math.MathContext
import java.math.RoundingMode
import javax.inject.Inject

class CurrencyConverterUseCase @Inject constructor(
        private val loadRatesUseCase: LoadRatesUseCase
) : BaseUseCase<Rate, List<Rate>> {

    private val currentBase = BehaviorSubject.create<Rate>()

    override fun setArg(arg: Rate) {
        currentBase.onNext(arg)
        loadRatesUseCase.setArg(arg)
    }

    override fun get(): Flowable<List<Rate>> {
        return Flowable.combineLatest(
                currentBase.toFlowable(BackpressureStrategy.LATEST),
                loadRatesUseCase.get(), BiFunction { base: Rate, rates: Rates ->
            Pair(base, rates)
        }).scan(emptyList(), BiFunction { previous, (newBase, rates) ->
            val converted = if (rates.base.code == newBase.code) {
                rates.currencies.map {
                    Rate(it.code, (newBase.value * it.value).setScale(2, RoundingMode.HALF_UP))
                }
            } else {
                previous
            }
            val sortedByDescending = converted.sortedByDescending {
                if (it.code == newBase.code) 1 else 0
            }
            sortedByDescending
        })
    }
}