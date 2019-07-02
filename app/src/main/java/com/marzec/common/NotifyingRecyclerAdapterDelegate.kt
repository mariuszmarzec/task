package com.marzec.common

import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class NotifyingRecyclerAdapterDelegate<T>(var value: T) : ReadWriteProperty<RecyclerView.Adapter<out RecyclerView.ViewHolder>, T> {
    override fun getValue(thisRef: RecyclerView.Adapter<out RecyclerView.ViewHolder>, property: KProperty<*>): T {
        return value
    }

    override fun setValue(thisRef: RecyclerView.Adapter<out RecyclerView.ViewHolder>, property: KProperty<*>, value: T) {
        this.value = value
        thisRef.notifyDataSetChanged()
    }
}