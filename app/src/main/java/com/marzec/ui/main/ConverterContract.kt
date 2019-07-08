package com.marzec.ui.main

import com.marzec.base.BasePresenter
import com.marzec.model.Rate
import com.marzec.model.RateViewItem

interface ConverterContract {

    interface View {
        fun showRates(rates: List<RateViewItem>)
        fun showError()

    }

    interface Presenter : BasePresenter<View> {
        fun load()
        fun setBaseCurrency(rate: Rate)

    }
}