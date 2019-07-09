package com.marzec.usecase

import com.marzec.base.BaseUseCase
import com.marzec.model.Rate
import com.marzec.model.Rates
import com.marzec.repository.ConverterRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoadRatesUseCase @Inject constructor(
        private val converterRepository: ConverterRepository
) : BaseUseCase<Rate, Rates> {

    private val currentBase = BehaviorSubject.create<Rate>()

    override fun setArg(arg: Rate) {
        currentBase.onNext(arg)
    }

    override fun get(): Flowable<Rates> {
        return currentBase.toFlowable(BackpressureStrategy.LATEST).switchMap { rate ->
            Flowable.interval(1, TimeUnit.SECONDS).startWith(0).switchMap {
                converterRepository.getRates(rate.code).toFlowable()
                        .subscribeOn(Schedulers.io())
            }
        }
    }
}