package com.example.linksdemo.API

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitClient {

    private const val BASE_URL = "https://api.inopenapp.com/api/v1/"
    private const val BEARER_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI"

    fun create() : Apiservice {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(createInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        return retrofit.create(Apiservice::class.java)
    }

    private fun createInterceptor(): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request : Request = chain.request().newBuilder()
                    .header("Authorization","Bearer $BEARER_TOKEN")
                    .build()
                return chain.proceed(request)
            }

        }

    }
}