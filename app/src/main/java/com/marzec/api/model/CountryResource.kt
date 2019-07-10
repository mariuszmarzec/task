package com.marzec.api.model

data class CountryResource(val name: String?, val alpha2Code: String?, val currencies: List<CurrencyResource?>?)

data class CurrencyResource(val code: String?, val name: String?, val symbol: String?)