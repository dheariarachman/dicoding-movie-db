package com.example.themovieapps.service

import com.example.themovieapps.misc.Misc
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var mRetrofit: Retrofit? = null

    val client: Retrofit?
        get() {
            if (mRetrofit == null) {
                mRetrofit = Retrofit.Builder().baseUrl(Misc.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build()
            }
            return this.mRetrofit
        }
}