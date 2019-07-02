package com.marzec.api

import com.marzec.api.model.RatesResource
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ConverterApi {

    @GET("latest")
    fun getCurrencies(@Query("base") base: String): Single<RatesResource>
}

//https://revolut.duckdns.org/latest?base=EUR