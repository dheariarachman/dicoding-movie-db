package com.example.themovieapps.model

data class SerialResponse(
    val page: Int,
    val results: List<ListSerial>,
    val total_pages: Int,
    val total_results: Int
)