package com.marzec.common

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.marzec.model.RateViewItem

class RateDiffUtil(
        private val oldItems: List<RateViewItem>,
        private val newItems: List<RateViewItem>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].code == newItems[newItemPosition].code
    }

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val old = oldItems[oldItemPosition]
        val new = newItems[newItemPosition]
        return Bundle().apply {
            putBoolean(KEY_ENABLED, new.editable)
            putString(KEY_CODE, new.code)
            if (new.value != old.value) putString(KEY_VALUE, new.value)
        }
    }

    companion object {
        const val KEY_ENABLED = "KEY_ENABLED"
        const val KEY_CODE = "KEY_CODE"
        const val KEY_VALUE = "KEY_VALUE"
    }
}