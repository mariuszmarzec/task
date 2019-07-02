package com.marzec.base

import io.reactivex.Flowable

interface BaseUseCase<R, T> {

    fun setArg(arg: R)
    fun get() : Flowable<T>
}