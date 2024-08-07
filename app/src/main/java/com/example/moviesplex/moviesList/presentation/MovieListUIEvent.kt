package com.example.moviesplex.moviesList.presentation

sealed interface MovieListUIEvent {
    data class Paginate(val category: String): MovieListUIEvent
    object Navigate: MovieListUIEvent
}