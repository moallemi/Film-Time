package io.fimltime.data.tmdb.movies

import io.filmtime.data.api.tmdb.TmdbMoviesRemoteSource
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail
import javax.inject.Inject

internal class TmdbMovieRepositoryImpl @Inject constructor(
  private val tmdbMoviesRemoteSource: TmdbMoviesRemoteSource,
) : TmdbMovieRepository {

  override suspend fun getMovieDetails(movieId: Int): VideoDetail =
    tmdbMoviesRemoteSource.getMovieDetails(movieId)

  override suspend fun getTrendingMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.getTrendingMovies()

  override suspend fun getPopularMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.getPopularMovies()

  override suspend fun getTopRatedMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.getTopRatedMovies()

  override suspend fun getNowPlayingMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.getNowPlayingMovies()
}
