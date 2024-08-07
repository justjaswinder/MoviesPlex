package com.example.moviesplex.moviesList.core.details.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.moviesplex.R
import com.example.moviesplex.moviesList.data.remote.MovieApi
import com.example.moviesplex.moviesList.util.RatingBar
import com.example.moviesplex.moviesList.util.getAverageColor

@Composable
fun DetailScreen(navBackStackEntry: NavBackStackEntry) {
    val detailsViewModel= hiltViewModel<DetailsViewModel>()
    val detailsState=detailsViewModel.detailState.collectAsState().value

    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL+detailsState.movie?.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state
    val posterImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL+detailsState.movie?.poster_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Column (modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
    ){
        if(imageState is AsyncImagePainter.State.Error){
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ){
                Icon( modifier = Modifier.size(70.dp),
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription = detailsState.movie?.title)

            }
        }
        if(imageState is AsyncImagePainter.State.Success){

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                painter = imageState.painter,
                contentDescription = detailsState.movie?.title,
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)){
            Box(modifier = Modifier
                .width(160.dp)
                .height(240.dp)
            ){
                if(posterImageState is AsyncImagePainter.State.Error){
                    Box (
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ){
                        Icon( modifier = Modifier.size(70.dp),
                            imageVector = Icons.Rounded.ImageNotSupported,
                            contentDescription = detailsState.movie?.title)

                    }
                }
                if(posterImageState is AsyncImagePainter.State.Success){

                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        painter = posterImageState.painter,
                        contentDescription = detailsState.movie?.title,
                        contentScale = ContentScale.Crop
                    )
                }}
                detailsState.movie?.let {movie->
                    Column (modifier = Modifier.fillMaxWidth()){
                        Text(modifier = Modifier.padding(start = 16.dp),
                            text = movie.title,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold)

                        Spacer(modifier = Modifier.height(16.dp))

                        Row (modifier = Modifier
                           .padding(start = 16.dp)
                        ){
                            RatingBar(
                                starsModifier = Modifier.size(18.dp),
                                rating = movie.vote_average/2
                            )
                            Text(modifier = Modifier.padding(start = 4.dp),
                                text = movie.vote_average.toString().take(3),
                                color = Color.LightGray,
                                fontSize = 14.sp,
                                maxLines = 1
                            )

                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(modifier = Modifier.padding(start = 16.dp),
                            text = stringResource(R.string.language) + movie.original_language,
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(modifier = Modifier.padding(start = 16.dp),
                            text = stringResource(R.string.release_date) + movie.release_date,
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = movie.vote_count.toString() + stringResource(R.string.votes),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))


            Text(modifier = Modifier.padding(start = 16.dp),
                text = stringResource(R.string.overview),
                fontSize = 19.sp,
                fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(8.dp))
            detailsState.movie?.let { movie ->
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = movie.overview,
                    fontSize = 16.sp
                )
            }
                Spacer(modifier = Modifier.height(32.dp))




    }
}