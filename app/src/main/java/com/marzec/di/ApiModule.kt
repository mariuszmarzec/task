package com.marzec.di

import com.marzec.api.ConverterApi
import com.marzec.api.CountriesApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    @Converter
    fun provideRetrofitForConverter(httpClient: OkHttpClient): Retrofit = Retrofit.Builder()
            .client(httpClient)
            .baseUrl("https://revolut.duckdns.org/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Countries
    fun provideRetrofitForCountries(httpClient: OkHttpClient): Retrofit = Retrofit.Builder()
            .client(httpClient)
            .baseUrl("https://restcountries.eu/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideConverterApi(@Converter retrofit: Retrofit): ConverterApi = retrofit.create(ConverterApi::class.java)

    @Provides
    @Singleton
    fun provideCountriesApi(@Countries retrofit: Retrofit): CountriesApi = retrofit.create(CountriesApi::class.java)
}