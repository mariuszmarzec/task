package com.marzec.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marzec.base.BaseViewHolder
import com.marzec.common.NotifyingRecyclerAdapterDelegate
import com.marzec.model.Rate
import com.marzec.view.RateView

typealias OnRateClickListener = (Rate) -> Unit

class ConverterAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    var onRateClickListener: OnRateClickListener? = null
    var rates: List<Rate> by NotifyingRecyclerAdapterDelegate(emptyList())

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return RateViewHolder(RateView(parent.context).apply { layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ) })
    }

    override fun getItemCount() = rates.size

    override fun getItemId(position: Int): Long {
        return rates[position].code.hashCode().toLong()
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    private inner class RateViewHolder(private val rateView: RateView) : BaseViewHolder(rateView) {

        val onClickListener = View.OnClickListener {
            if (adapterPosition > 0) {
                onRateClickListener?.invoke(rates[adapterPosition])
            }
        }

        override fun onBind(position: Int) {
            rateView.bind(rates[position])
            rateView.setOnClickListener(onClickListener)
        }
    }
}
