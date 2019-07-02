package com.marzec.base

interface BasePresenter<V> {

    fun attach(view: V)
    fun detach()
}