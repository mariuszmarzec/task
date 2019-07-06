package com.marzec.common

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class NotifyingRecyclerAdapterDelegate<T>(var value: List<T>) : ReadWriteProperty<RecyclerView.Adapter<out RecyclerView.ViewHolder>, List<T>> {
    override fun getValue(thisRef: RecyclerView.Adapter<out RecyclerView.ViewHolder>, property: KProperty<*>): List<T> {
        return value
    }

    override fun setValue(thisRef: RecyclerView.Adapter<out RecyclerView.ViewHolder>, property: KProperty<*>, value: List<T>) {
        val old = this.value
        this.value = value
        val dataClassDiffUtil = DataClassDiffUtil(old, value)
        val diffResult = DiffUtil.calculateDiff(dataClassDiffUtil)
        diffResult.dispatchUpdatesTo(thisRef)
    }
}

class DataClassDiffUtil<T>(
        private val oldItems: List<T>,
        private val newItems: List<T>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }
}