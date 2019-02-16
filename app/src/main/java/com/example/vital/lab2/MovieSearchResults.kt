package com.example.vital.lab2

data class MovieSearchResults (
        val Search: ArrayList<Movie>?,
        val totalResults: String?,
        val Response: String?
)

data class Movie (
        val Title: String,
        val Year: String,
        val imdbID: String,
        val Type: String,
        val Poster: String
)