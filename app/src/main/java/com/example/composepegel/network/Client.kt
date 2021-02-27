package com.example.composepegel.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber

interface Client {

    fun get(): Retrofit
}

class ClientImpl : Client {

    @ExperimentalSerializationApi
    override fun get(): Retrofit {
        val logging = HttpLoggingInterceptor { message -> Timber.tag("OkHttp").d(message) }
        logging.level = HttpLoggingInterceptor.Level.BODY
        val contentType = "application/json".toMediaType()
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        return Retrofit.Builder()
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                coerceInputValues = true
                encodeDefaults = true
                isLenient = true
            }.asConverterFactory(contentType))
            .client(client)
            .baseUrl("https://www.pegelonline.wsv.de/webservices/rest-api/v2/")
            .build()
    }
}