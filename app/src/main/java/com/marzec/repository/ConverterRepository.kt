package com.marzec.repository

import com.marzec.api.ConverterApi
import com.marzec.extensions.whenNotNull
import com.marzec.model.Rate
import io.reactivex.Single
import javax.inject.Inject

class ConverterRepository @Inject constructor(
        private val api: ConverterApi
) {

    fun getRates(code: String): Single<List<Rate>> {
        return api.getCurrencies(code.toUpperCase()).map { resource ->
            listOf(Rate(code, 1.0)) + resource.rates?.entries?.mapNotNull {
                whenNotNull(it.key, it.value) { key, value ->
                    Rate(key, value)
                }
            }.orEmpty().toList()
        }
    }
}