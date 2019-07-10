package com.marzec.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.marzec.R
import com.marzec.base.BaseViewHolder
import com.marzec.common.OnTextChangeListener
import com.marzec.common.RateDiffUtil
import com.marzec.extensions.toBigDecimal
import com.marzec.model.Rate
import com.marzec.model.RateViewItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_rate.view.*
import com.marzec.common.DecimalDigitsInputFilter
import android.text.InputFilter

typealias OnRateClickListener = (RateViewItem) -> Unit
typealias OnBaseRateValueChangeListener = (RateViewItem) -> Unit

class ConverterAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val picasso = Picasso.get()

    var onRateClickListener: OnRateClickListener? = null
    var onBaseRateValueChangeListener: OnBaseRateValueChangeListener? = null
    private var rates: List<RateViewItem> = emptyList()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RateViewHolder(inflater.inflate(R.layout.view_rate, parent, false).apply {
            rateValue.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(50, 2))
        })
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

    fun update(new: List<RateViewItem>) {
        val old = rates
        val dataClassDiffUtil = RateDiffUtil(old, new)
        val diffResult = DiffUtil.calculateDiff(dataClassDiffUtil, true)
        diffResult.dispatchUpdatesTo(this)
        rates = new
    }

    private inner class RateViewHolder(private val rateView: View) : BaseViewHolder(rateView) {

        val onClickListener = View.OnClickListener {
            if (adapterPosition > 0) {
                onRateClickListener?.invoke(rates[adapterPosition])
            }
        }

        val onValueListener = object : OnTextChangeListener() {
            override fun  afterTextChanged(editable: Editable?) {
                val text = editable.toString()
                if (adapterPosition == 0) {
                    val rate = rates[adapterPosition].copy(value = text)
                    rates = rates.toMutableList().apply { removeAt(0); add(0, rate) }
                    onBaseRateValueChangeListener?.invoke(rate)
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
            rateView.rateValue.setText(rate.value)
            rateView.rateValue.isEnabled = rate.editable
            rateView.countryFlag
            rateView.currencyName.text = rate.info?.name.orEmpty()
            loadImage(rateView.countryFlag, rate.info?.flagImg)
            rateView.rateValue.addTextChangedListener(onValueListener)
        }

        override fun onBind(position: Int, payloadChange: Bundle) {
            val value = payloadChange.getString(RateDiffUtil.KEY_VALUE)
            val enabled = payloadChange.getBoolean(RateDiffUtil.KEY_ENABLED)
            val code = payloadChange.getString(RateDiffUtil.KEY_CODE)
            with(itemView.rateValue) {
                removeTextChangedListener(onValueListener)
                isEnabled = enabled
                value?.let { setText(it) }
                addTextChangedListener(onValueListener)
            }
            code?.let { itemView.rateCode.text = it }
            if (payloadChange.containsKey(RateDiffUtil.KEY_CURRENCY_NAME)) {
                itemView.currencyName.text = payloadChange.getString(RateDiffUtil.KEY_CURRENCY_NAME).orEmpty()
            }
            if (payloadChange.containsKey(RateDiffUtil.KEY_COUNTRY_FLAG)) {
                loadImage(rateView.countryFlag, payloadChange.getString(RateDiffUtil.KEY_COUNTRY_FLAG))
            }
        }

        private fun loadImage(imageView: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                picasso.load(url).into(imageView)
            } else {
                imageView.setImageDrawable(null)
            }
        }
    }
}
