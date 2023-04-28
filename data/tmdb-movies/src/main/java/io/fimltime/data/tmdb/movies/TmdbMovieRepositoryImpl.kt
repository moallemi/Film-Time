package io.fimltime.data.tmdb.movies

import io.filmtime.data.api.tmdb.TmdbMoviesRemoteSource
import io.filmtime.data.model.VideoThumbnail

internal class TmdbMovieRepositoryImpl(
  private val tmdbMoviesRemoteSource: TmdbMoviesRemoteSource,
) : TmdbMovieRepository {

  override suspend fun getMovieDetails(movieId: Int): VideoThumbnail =
    tmdbMoviesRemoteSource.getMovieDetails(movieId)
}
