package com.marzec.ui.main

import android.util.Log
import com.marzec.extensions.ioTransform
import com.marzec.model.Rate
import com.marzec.usecase.CurrencyConverterUseCase
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ConverterPresenter @Inject constructor(
        private val converterUseCase: CurrencyConverterUseCase
) : ConverterContract.Presenter {

    private val defaultRate = Rate("EUR", 1.0)

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
        disposable = converterUseCase.get()
                .ioTransform()
                .subscribe({ view?.showRates(it) }, { Log.d("ERROR", it.message, it) })
    }
}