package com.marzec.ui.main

import android.util.Log
import com.marzec.extensions.ioTransform
import com.marzec.model.Rate
import com.marzec.model.RateViewItem
import com.marzec.usecase.CurrencyConverterUseCase
import io.reactivex.disposables.Disposable
import java.math.BigDecimal
import java.text.DecimalFormat
import javax.inject.Inject

class ConverterPresenter @Inject constructor(
        private val converterUseCase: CurrencyConverterUseCase
) : ConverterContract.Presenter {

    private val defaultRate = Rate("EUR", BigDecimal.valueOf(1))

    private var disposable: Disposable? = null
    private var view: ConverterContract.View? = null

    override fun attach(view: ConverterContract.View) {
        this.view = view
    }

    override fun detach() {
        disposable?.dispose()
        view = null
    }

    override fun load() {
        converterUseCase.setArg(defaultRate)
        disposable = loadCurrencies()
                .ioTransform()
                .subscribe({ view?.showRates(it) }, { Log.d("ERROR", it.message, it) })
    }

    private fun loadCurrencies() =
            converterUseCase.get()
                    .map { rates ->
                        val df = DecimalFormat().apply {
                            maximumFractionDigits = 2
                            minimumFractionDigits = 0
                            isGroupingUsed = false
                        }
                        rates.mapIndexed { index, rate ->
                            RateViewItem(rate.code, df.format(rate.value), index == 0)
                        }
                    }

    override fun setBaseCurrency(rate: Rate) {
        converterUseCase.setArg(rate)
    }
}