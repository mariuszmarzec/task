package com.marzec.ui.main

import com.marzec.base.BasePresenter
import com.marzec.model.Rate

interface ConverterContract {

    interface View {
        fun showRates(rates: List<Rate>)

    }

    interface Presenter : BasePresenter<View> {
        fun load()
        fun setBaseCurrency(rate: Rate)

    }
}