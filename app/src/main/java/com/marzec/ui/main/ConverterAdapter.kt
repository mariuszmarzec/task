package com.marzec.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marzec.R
import com.marzec.base.BaseViewHolder
import com.marzec.common.NotifyingRecyclerAdapterDelegate
import com.marzec.common.OnTextChangeInteractionListener
import com.marzec.common.OnTextChangeListener
import com.marzec.model.Rate
import kotlinx.android.synthetic.main.view_rate.view.*
import java.math.BigDecimal

typealias OnRateClickListener = (Rate) -> Unit
typealias OnBaseRateValueChangeListener = (Rate) -> Unit

class ConverterAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    var onRateClickListener: OnRateClickListener? = null
    var onBaseRateValueChangeListener: OnBaseRateValueChangeListener? = null
    var rates: List<Rate> by NotifyingRecyclerAdapterDelegate(emptyList())

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RateViewHolder(inflater.inflate(R.layout.view_rate, parent, false))
    }

    override fun getItemCount() = rates.size

    override fun getItemId(position: Int): Long {
        return rates[position].code.hashCode().toLong()
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    private inner class RateViewHolder(private val rateView: View) : BaseViewHolder(rateView) {

        val onClickListener = View.OnClickListener {
            if (adapterPosition > 0) {
                onRateClickListener?.invoke(rates[adapterPosition])
            }
        }

        val onValueListener = OnTextChangeInteractionListener(object : OnTextChangeListener() {
            override fun onAfterTextChange(text: String) {
                if (adapterPosition == 0) {
                    val newBase = rates[adapterPosition].copy(value = BigDecimal(text))
                    onBaseRateValueChangeListener?.invoke(newBase)
                }
            }
        })

        init {
            rateView.setOnClickListener(onClickListener)
        }

        override fun onBind(position: Int) {
            val rate = rates[position]
            rateView.rateCode.text = rate.code
            rateView.rateValue.setText("${rate.value}")
            rateView.rateValue.isEnabled = position == 0
            rateView.rateValue.setOnTouchListener(onValueListener)
            rateView.rateValue.addTextChangedListener(onValueListener)
        }
    }
}
