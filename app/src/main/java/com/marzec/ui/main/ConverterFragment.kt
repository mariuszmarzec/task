package com.marzec.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marzec.R
import com.marzec.base.BaseFragment
import javax.inject.Inject

class ConverterFragment : BaseFragment(), ConverterContract.View {

    @Inject
    lateinit var presenter: ConverterContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_converter, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
    }

    override fun onPause() {
        presenter.detach()
        super.onPause()
    }
}
