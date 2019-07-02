package com.marzec.extensions

fun <T1: Any, T2: Any, R: Any> whenNotNull(ob: T1?, ob2: T2?, block: (T1, T2)->R?): R? =
        if (ob != null && ob2 != null) block(ob, ob2) else null
