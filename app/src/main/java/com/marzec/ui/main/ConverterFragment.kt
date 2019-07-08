package com.marzec.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.marzec.R
import com.marzec.base.BaseFragment
import com.marzec.model.RateViewItem
import kotlinx.android.synthetic.main.fragment_converter.*
import org.jetbrains.anko.alert
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
        converterAdapter.onBaseRateValueChangeListener = { presenter.setBaseCurrency(it) }
    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
        presenter.load()
    }

    override fun showRates(rates: List<RateViewItem>) {
        converterAdapter.rates = rates
    }

    override fun showError() {
        context?.alert(R.string.dialog_message_error_occurred, R.string.dialog_title_error) {
            positiveButton(R.string.button_try_again) {
                it.dismiss()
                presenter.load()
            }
            negativeButton(R.string.button_exit_app) {
                activity?.finish()
            }
            isCancelable = false
            show()
        }
    }

    override fun onPause() {
        presenter.detach()
        super.onPause()
    }
}
