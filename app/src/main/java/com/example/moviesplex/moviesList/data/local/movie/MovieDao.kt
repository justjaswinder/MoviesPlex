package com.example.moviesplex.moviesList.data.local.movie

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MovieDao {

    @Upsert
    suspend fun upsertMovieList(movieList : List<MovieEntity>)

    @Query("Select * from MovieEntity WHERE id = :id")
    suspend fun getMovieById(id:Int): MovieEntity

    @Query("Select * from MovieEntity WHERE category = :category")
    suspend fun getMovieListByCategory(category:String): List<MovieEntity>
}