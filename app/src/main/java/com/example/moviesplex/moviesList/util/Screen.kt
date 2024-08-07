package com.example.moviesplex.moviesList.util

sealed class Screen(val route:String){
    object Home: Screen("main")
    object PopularMovieList: Screen("popularMovie")
    object UpcomingMovieList: Screen("upcomingMovie")
    object Details: Screen("details")
}