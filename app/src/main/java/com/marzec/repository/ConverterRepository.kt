package com.marzec.repository

import com.marzec.api.ConverterApi
import com.marzec.extensions.whenNotNull
import com.marzec.model.Rate
import com.marzec.model.Rates
import io.reactivex.Single
import java.math.BigDecimal
import javax.inject.Inject

class ConverterRepository @Inject constructor(
        private val api: ConverterApi
) {

    fun getRates(code: String): Single<Rates> {
        return api.getCurrencies(code.toUpperCase()).map { resource ->
            val base = Rate(code, BigDecimal.valueOf(1))
            Rates(base, listOf(base) + resource.rates?.entries?.mapNotNull {
                whenNotNull(it.key, it.value) { key, value ->
                    if (key.isNotEmpty()) Rate(key, BigDecimal.valueOf(value)) else null
                }
            }.orEmpty())
        }
    }
}