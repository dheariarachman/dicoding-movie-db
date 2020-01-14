package com.example.themovieapps.service

import com.example.themovieapps.model.MovieResponse
import com.example.themovieapps.model.SerialResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDBService {
    @GET("/3/discover/movie?")
    fun getAllMovieData(@Query("api_key") api_key: String, @Query("language") language: String): Call<MovieResponse>

    @GET("/3/discover/tv?")
    fun getAllSerialData(@Query("api_key") api_key: String, @Query("language") language: String): Call<SerialResponse>
}