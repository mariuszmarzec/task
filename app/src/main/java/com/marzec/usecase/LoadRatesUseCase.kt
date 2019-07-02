package com.marzec.usecase

import com.marzec.base.BaseUseCase
import com.marzec.model.Rate
import com.marzec.repository.ConverterRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoadRatesUseCase @Inject constructor(
        private val converterRepository: ConverterRepository
) : BaseUseCase<Rate, List<Rate>> {

    private val currentBase = BehaviorSubject.create<Rate>()

    override fun setArg(arg: Rate) {
        currentBase.onNext(arg)
    }

    override fun get(): Flowable<List<Rate>> {
        return currentBase.toFlowable(BackpressureStrategy.LATEST).switchMap { rate ->
            Flowable.interval(1, TimeUnit.SECONDS).switchMap {
                converterRepository.getRates(rate.code).toFlowable()
            }
        }
    }
}