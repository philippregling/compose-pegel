package com.example.composepegel.network

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import timber.log.Timber
import javax.inject.Inject

interface Client {

    fun get(): PegelAPI
}

class ClientImpl @Inject constructor(@ApplicationContext private val context: Context) : Client {

    @ExperimentalSerializationApi
    override fun get(): PegelAPI {
        val logging = HttpLoggingInterceptor { message -> Timber.tag("OkHttp").d(message) }
        logging.level = HttpLoggingInterceptor.Level.BODY
        val contentType = "application/json".toMediaType()
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
//            .cache(Cache(context.cacheDir, 10L * 1024L * 1024L))
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
            .build().create()
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ClientModule {

    @Binds
    abstract fun bindClient(
        clientImpl: ClientImpl
    ): Client
}