package io.fimltime.data.tmdb.movies

import io.filmtime.data.api.tmdb.TmdbMoviesRemoteSource
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail
import javax.inject.Inject

internal class TmdbMovieRepositoryImpl @Inject constructor(
  private val tmdbMoviesRemoteSource: TmdbMoviesRemoteSource,
) : TmdbMovieRepository {

  override suspend fun getMovieDetails(movieId: Int): VideoDetail =
    tmdbMoviesRemoteSource.getMovieDetails(movieId)

  override suspend fun getTrendingMovies(): List<VideoThumbnail> =
    tmdbMoviesRemoteSource.getTrendingMovies()

  override suspend fun getPopularMovies(): List<VideoThumbnail> =
    tmdbMoviesRemoteSource.getPopularMovies()

  override suspend fun getTopRatedMovies(): List<VideoThumbnail> =
    tmdbMoviesRemoteSource.getTopRatedMovies()

  override suspend fun getNowPlayingMovies(): List<VideoThumbnail> =
    tmdbMoviesRemoteSource.getNowPlayingMovies()
}
