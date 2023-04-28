package io.filmtime.data.api.tmdb

import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.network.TmdbMoviesService

internal class TmdbMoviesMoviesRemoteSourceImpl(
  private val tmdbMoviesService: TmdbMoviesService,
) : TmdbMoviesRemoteSource {

  override suspend fun getMovieDetails(movieId: Int): VideoThumbnail =
    tmdbMoviesService.getMovieDetails(movieId)
      .toVideoThumbnail()
}
