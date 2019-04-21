package com.example.vital.lab2

import android.arch.persistence.room.*

data class MovieSearchResults (
        val Search: List<Movie>?,
        val totalResults: String?,
        val Response: String?
)

@Entity(tableName = "Movie")
data class Movie (
        @PrimaryKey(autoGenerate = true) var id: Long?,
        @ColumnInfo(name = "Title") var Title: String,
        @ColumnInfo(name = "Year") var Year: String,
        @ColumnInfo(name = "imdbID") var imdbID: String,
        @ColumnInfo(name = "Type") var Type: String,
        @ColumnInfo(name = "Poster") var Poster: String
) {
    constructor(): this(null, "", "", "", "", "")
}