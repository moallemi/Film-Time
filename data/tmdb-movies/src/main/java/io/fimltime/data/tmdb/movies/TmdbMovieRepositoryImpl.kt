package io.fimltime.data.tmdb.movies

import io.filmtime.data.api.tmdb.TmdbMoviesRemoteSource
import io.filmtime.data.model.VideoDetail
import javax.inject.Inject

internal class TmdbMovieRepositoryImpl @Inject constructor(
  private val tmdbMoviesRemoteSource: TmdbMoviesRemoteSource,
) : TmdbMovieRepository {

  override suspend fun getMovieDetails(movieId: Int): VideoDetail =
    tmdbMoviesRemoteSource.getMovieDetails(movieId)
}
