package com.marzec.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.marzec.R
import com.marzec.base.BaseFragment
import com.marzec.model.Rate
import kotlinx.android.synthetic.main.fragment_converter.*
import javax.inject.Inject

class ConverterFragment : BaseFragment(), ConverterContract.View {

    @Inject
    lateinit var presenter: ConverterContract.Presenter

    private lateinit var converterAdapter: ConverterAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_converter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ConverterAdapter().also { converterAdapter = it }
        converterAdapter.onRateClickListener = { presenter.setBaseCurrency(it) }
    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
        presenter.load()
    }

    override fun showRates(rates: List<Rate>) {
        converterAdapter.rates = rates
    }

    override fun onPause() {
        presenter.detach()
        super.onPause()
    }
}
