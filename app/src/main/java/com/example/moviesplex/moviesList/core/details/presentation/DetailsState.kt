package com.example.moviesplex.moviesList.core.details.presentation

import com.example.moviesplex.moviesList.domain.model.Movie

data class DetailsState(
    val isLoading:Boolean=false,
    val movie:Movie?=null

)
