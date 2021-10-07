package com.nishimura.cvshealthcodechallenge.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.flickr.com")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .client(OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }.build())
        .build()
    val flickrService: FlickrService = retrofit.create(FlickrService::class.java)
}