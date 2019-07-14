package com.marzec.ui.main

import android.util.Log
import com.marzec.extensions.ioTransform
import com.marzec.extensions.toBigDecimal
import com.marzec.model.CurrencyDataStorage
import com.marzec.model.Rate
import com.marzec.model.RateViewItem
import com.marzec.usecase.CurrencyConverterUseCase
import com.marzec.usecase.LoadCurrenciesDataUseCase
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import javax.inject.Inject

class ConverterPresenter @Inject constructor(
        private val converterUseCase: CurrencyConverterUseCase,
        private val loadCurrenciesDataUseCase : LoadCurrenciesDataUseCase
) : ConverterContract.Presenter {

    private var defaultRate = Rate("EUR", BigDecimal.valueOf(1))
    private var baseViewItem: RateViewItem? = null
    private var baseRate: Rate? = null

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
        disposable?.dispose()
        disposable = loadCurrencies()
                .ioTransform()
                .subscribe(
                        {
                            view?.showRates(it)
                        },
                        {
                            Log.d(ConverterPresenter::class.java.simpleName, it.message, it)
                            view?.showError()
                        })
        if (baseRate == null) {
            baseViewItem = null
            baseRate = defaultRate
        }
        baseRate?.let { converterUseCase.setArg(it) }
    }

    private fun loadCurrencies() = Flowable.combineLatest(
            converterUseCase.get(),
            loadCurrenciesDataUseCase.get(),
            BiFunction { rates: List<Rate>, currencyData: CurrencyDataStorage ->
                val df = DecimalFormat().apply {
                    maximumFractionDigits = 2
                    minimumFractionDigits = 0
                    decimalFormatSymbols = DecimalFormatSymbols.getInstance().apply { decimalSeparator = '.' }
                    isGroupingUsed = false
                }
                rates.mapIndexed { index, rate ->
                    val currentBaseViewItem = baseViewItem
                    val value = if (index == 0 && currentBaseViewItem != null) {
                        currentBaseViewItem.value
                    } else {
                        df.format(rate.value)
                    }
                    RateViewItem(rate.code, value, index == 0, currencyData.get(rate.code))
                }
            })

    override fun setBaseCurrency(rate: RateViewItem) {
        baseViewItem = rate
        rate.value.toBigDecimal()?.let { baseRate = Rate(rate.code, it) }
        baseRate?.let { converterUseCase.setArg(it) }
    }
}