package com.example.moviesplex.moviesList.data.repository

import coil.network.HttpException
import com.example.moviesplex.moviesList.data.local.movie.MovieDatabase
import com.example.moviesplex.moviesList.data.mappers.toMovie
import com.example.moviesplex.moviesList.data.mappers.toMovieEntity
import com.example.moviesplex.moviesList.data.remote.MovieApi
import com.example.moviesplex.moviesList.domain.model.Movie
import com.example.moviesplex.moviesList.domain.repository.MovieListRepository
import com.example.moviesplex.moviesList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val database: MovieDatabase
) : MovieListRepository {
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            val localMovieList = database.movieDao.getMovieListByCategory(category)

            val shouldLoadLocal = localMovieList.isNotEmpty() && !forceFetchFromRemote

            if (shouldLoadLocal) {
                emit(Resource.Success(
                    data = localMovieList.map { movieEntity ->
                        movieEntity.toMovie(category)
                    }
                ))
                emit(Resource.Loading(false))
                return@flow
            }
            val movieListFromApi = try {
                movieApi.getMoviesList(category, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }
            val movieEntities = movieListFromApi.results.let { it ->
                it.map { movieDto ->
                    movieDto.toMovieEntity(category)
                }
            }

            database.movieDao.upsertMovieList(movieEntities)
            emit(Resource.Success(
                movieEntities.map { it.toMovie(category) }
            ))
            emit(Resource.Loading(false))
            return@flow
        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))

            val movieEntity = database.movieDao.getMovieById(id)
            if(movieEntity!=null){
                emit(Resource.Success(
                    movieEntity.toMovie(movieEntity.category  )
                ))
                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error("Error no such Movie"))
            emit(Resource.Loading(false))
        }
    }
}