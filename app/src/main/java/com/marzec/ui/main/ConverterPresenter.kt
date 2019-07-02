package com.marzec.ui.main

import com.marzec.usecase.CurrencyConverterUseCase
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ConverterPresenter @Inject constructor(
        private val converterUseCase: CurrencyConverterUseCase
) : ConverterContract.Presenter {

    private var disposable: Disposable? = null
    private var view: ConverterContract.View? = null

    override fun attach(view: ConverterContract.View) {
        this.view = view
    }

    override fun detach() {
        disposable?.dispose()
    }
}