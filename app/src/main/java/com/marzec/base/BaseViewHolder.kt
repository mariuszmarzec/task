package com.marzec.base

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    open fun onBind(position: Int) = Unit

    open fun onBind(position: Int, payloadChange: Bundle) = Unit
}