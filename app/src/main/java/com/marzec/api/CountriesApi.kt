package com.marzec.api

import com.marzec.api.model.CountryResource
import io.reactivex.Single
import retrofit2.http.GET

interface CountriesApi {

    @GET("rest/v2/all")
    fun getAllCountries(): Single<List<CountryResource>>
}