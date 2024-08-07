package com.example.moviesplex.moviesList.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviesplex.moviesList.presentation.MovieListState
import com.example.moviesplex.moviesList.presentation.MovieListUIEvent
import com.example.moviesplex.moviesList.presentation.components.MovieItem
import com.example.moviesplex.moviesList.util.Category

@Composable
fun UpcomingMoviesScreen(
    movieListState: MovieListState,
    navHostController: NavHostController,
    onEvent: (MovieListUIEvent) -> Unit
) {
    if (movieListState.upcomingMovieList.isEmpty()) {
        Box( modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center){
            CircularProgressIndicator()
        }

    } else {
        LazyVerticalGrid(columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
        ) {
            items(movieListState.upcomingMovieList.size){ index ->
                MovieItem(
                    movie = movieListState.upcomingMovieList[index],
                    navHostController = navHostController
                )
                Spacer(modifier = Modifier.height(16.dp))

                if(index >= movieListState.upcomingMovieList.size -1 && !movieListState.isLoading){
                    onEvent(MovieListUIEvent.Paginate(Category.POPULAR))
                }


            }

        }

    }
}