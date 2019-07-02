package com.marzec.usecase

import com.marzec.base.BaseUseCase
import com.marzec.model.Rate
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class CurrencyConverterUseCase @Inject constructor(
        private val loadRatesUseCase: LoadRatesUseCase
) : BaseUseCase<Rate, List<Rate>> {

    private val currentBase = PublishSubject.create<Rate>()

    override fun setArg(arg: Rate) {
        currentBase.onNext(arg)
        loadRatesUseCase.setArg(arg)
    }

    override fun get(): Flowable<List<Rate>> {
        return Flowable.combineLatest(
                currentBase.toFlowable(BackpressureStrategy.LATEST),
                loadRatesUseCase.get(), BiFunction { base, rates ->
            rates.map { Rate(it.code, base.value * it.value) }
        })
    }
}