package com.marzec.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.marzec.R
import com.marzec.base.BaseViewHolder
import com.marzec.common.OnTextChangeListener
import com.marzec.common.RateDiffUtil
import com.marzec.extensions.toBigDecimal
import com.marzec.model.Rate
import com.marzec.model.RateViewItem
import kotlinx.android.synthetic.main.view_rate.view.*
import java.math.BigDecimal

typealias OnRateClickListener = (Rate) -> Unit
typealias OnBaseRateValueChangeListener = (Rate) -> Unit

class ConverterAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    var onRateClickListener: OnRateClickListener? = null
    var onBaseRateValueChangeListener: OnBaseRateValueChangeListener? = null
    var rates: List<RateViewItem> = emptyList()
        set(value) {
            val old = field
            val dataClassDiffUtil = RateDiffUtil(old, value)
            val diffResult = DiffUtil.calculateDiff(dataClassDiffUtil, true)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }

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

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            holder.onBind(position, payloads.first() as Bundle)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    private inner class RateViewHolder(private val rateView: View) : BaseViewHolder(rateView) {

        val onClickListener = View.OnClickListener {
            if (adapterPosition > 0) {
                onRateClickListener?.invoke(rates[adapterPosition].toRate())
            }
        }

        val onValueListener = object : OnTextChangeListener() {
            override fun onAfterTextChange(text: String) {
                if (adapterPosition == 0) {
                    val rate = rates[adapterPosition]
                    val newBase = Rate(rate.code, text.toBigDecimal(BigDecimal("0")))
                    onBaseRateValueChangeListener?.invoke(newBase)
                }
            }
        }

        init {
            rateView.setOnClickListener(onClickListener)
        }

        override fun onBind(position: Int) {
            val rate = rates[position]
            rateView.rateValue.removeTextChangedListener(onValueListener)
            rateView.rateCode.text = rate.code
            if (rateView.rateValue.text.toString() != rate.value) {
                rateView.rateValue.setText(rate.value)
            }
            rateView.rateValue.isEnabled = rate.editable
            rateView.rateValue.addTextChangedListener(onValueListener)
        }

        override fun onBind(position: Int, payloadChange: Bundle) {
            val value = payloadChange.getString(RateDiffUtil.KEY_VALUE)
            val enabled = payloadChange.getBoolean(RateDiffUtil.KEY_ENABLED)
            val code = payloadChange.getString(RateDiffUtil.KEY_CODE)
            with(itemView.rateValue) {
                removeTextChangedListener(onValueListener)
                isEnabled = enabled
                if (rateView.rateValue.text.toString() != value) {
                    value?.let { setText(it) }
                }
                addTextChangedListener(onValueListener)
            }
            code?.let { itemView.rateCode.text = it }
        }
    }
}
