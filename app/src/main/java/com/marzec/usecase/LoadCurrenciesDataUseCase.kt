package com.marzec.usecase

import com.marzec.base.BaseUseCase
import com.marzec.model.CurrencyData
import com.marzec.model.CurrencyDataStorage
import com.marzec.repository.CountriesRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.ReplaySubject
import javax.inject.Inject

class LoadCurrenciesDataUseCase @Inject constructor(
        private val countriesRepository: CountriesRepository
) : BaseUseCase<Unit, CurrencyDataStorage> {

    private val subject = ReplaySubject.create<CurrencyDataStorage>(1)

    override fun setArg(arg: Unit) = Unit

    override fun get(): Flowable<CurrencyDataStorage> {
        return if (subject.value == null) {
        countriesRepository.getCountriesData().map { currencies ->
            CurrencyDataStorage(currencies.map { it.code to it }.toMap().toMutableMap().apply {
                put("EUR", CurrencyData(
                        "EUR",
                        "Euro",
                        "",
                        "https://www.zielonysklep.pl/1214-large_default/flaga-unii-europejskiej-.jpg"
                ))
                put("SGD", CurrencyData(
                        "SGD",
                        "Singapore dollar",
                        "",
                        "https://www.countryflags.io/SG/shiny/64.png"
                ))
            })
        }.doOnSuccess { subject.onNext(it) }
                .toFlowable()
        } else {
            subject.toFlowable(BackpressureStrategy.LATEST)
        }
    }
}