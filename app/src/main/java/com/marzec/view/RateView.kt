package com.marzec.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.marzec.R
import com.marzec.model.Rate
import kotlinx.android.synthetic.main.view_rate.view.*

class RateView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_rate, this)
    }

    fun bind(rate: Rate) {
        rateCode.text = rate.code
        rateValue.setText(rate.value.toString())
    }
}