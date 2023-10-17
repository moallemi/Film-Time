package io.fimltime.data.tmdb.movies

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.filmtime.data.api.tmdb.TmdbMoviesRemoteSource
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail
import kotlinx.coroutines.flow.Flow
import paging.MoviePagingSource
import javax.inject.Inject

internal class TmdbMovieRepositoryImpl @Inject constructor(
  private val tmdbMoviesRemoteSource: TmdbMoviesRemoteSource,

) : TmdbMovieRepository {

  override suspend fun getMovieDetails(movieId: Int): Result<VideoDetail, GeneralError> =
    tmdbMoviesRemoteSource.getMovieDetails(movieId)

  override suspend fun getTrendingMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.getTrendingMovies(1)

  override  fun getAllTrendingMovies(): Flow<PagingData<VideoThumbnail>> {
   return Pager(
     config = PagingConfig(
       pageSize = 10,
       prefetchDistance = 20,
       enablePlaceholders = false,
       initialLoadSize = 10
     ),
     pagingSourceFactory = {
       MoviePagingSource(tmdbMoviesRemoteSource)
     }
   ).flow
  }


  override suspend fun getPopularMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.getPopularMovies(1)

  override suspend fun getTopRatedMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.getTopRatedMovies(1)

  override suspend fun getNowPlayingMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.getNowPlayingMovies(1)
}
