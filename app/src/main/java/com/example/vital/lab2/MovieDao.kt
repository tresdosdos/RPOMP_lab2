package com.example.vital.lab2

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface MovieDao {
    @Query("SELECT * FROM Movie")
    fun getAll(): List<Movie>

    @Insert()
    fun insert(movie: Movie)

    @Query("DELETE FROM Movie")
    fun deleteAll()
}