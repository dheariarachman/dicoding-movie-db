package com.example.themovieapps.model

data class MovieResponse(
    val page: Int,
    val results: ArrayList<ListMovie>,
    val total_pages: Int,
    val total_results: Int
)