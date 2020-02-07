package com.example.themovieapps.service

import com.example.themovieapps.model.MovieResponse
import com.example.themovieapps.model.SerialResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDBService {

    /**
     * @author Dhea Aria Rachman
     */

    /**
     * Movie API Service
     */

    /**
     * get All Movie List
     * @param api_key
     * @param language
     */
    @GET("/3/discover/movie?")
    fun getAllMovieData(@Query("api_key") api_key: String, @Query("language") language: String): Call<MovieResponse>


    /**
     * get Movie by Search
     * @param api_key
     * @param language
     * @param query
     */
    @GET("/3/search/movie?")
    fun getMovieBySearch(
        @Query("api_key") api_key: String, @Query("language") language: String, @Query(
            "query"
        ) query: String
    ): Call<MovieResponse>

    /**
     * Serial API Serice
     */

    /**
     * get All Serial List
     * @param api_key
     * @param language
     */
    @GET("/3/discover/tv?")
    fun getAllSerialData(@Query("api_key") api_key: String, @Query("language") language: String): Call<SerialResponse>

    /**
     * get Serial By Search
     * @param api_key
     * @param language
     * @param query
     */
    @GET("/3/search/tv?")
    fun getSerialBySearch(
        @Query("api_key") api_key: String, @Query("language") language: String, @Query(
            "query"
        ) query: String
    ): Call<SerialResponse>

    /**
     * get movie released today
     * @param api_key
     * @param primary_release_date_gte
     * @param primary_release_date_lte
     */

    @GET("3/discover/movie?")
    fun getReleasedMovieToday(
        @Query("api_key") api_key: String,
        @Query("primary_release_date.gte") primary_release_date_gte: String,
        @Query("primary_release_date.lte") primary_release_date_lte: String
    ) : Call<MovieResponse>
}