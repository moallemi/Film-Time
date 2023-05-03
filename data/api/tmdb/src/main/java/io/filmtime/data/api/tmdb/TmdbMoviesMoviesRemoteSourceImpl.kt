package io.filmtime.data.api.tmdb

import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.network.TmdbMoviesService
import javax.inject.Inject

internal class TmdbMoviesMoviesRemoteSourceImpl @Inject constructor(
  private val tmdbMoviesService: TmdbMoviesService,
) : TmdbMoviesRemoteSource {

  override suspend fun getMovieDetails(movieId: Int): VideoThumbnail =
    tmdbMoviesService.getMovieDetails(movieId)
      .toVideoThumbnail()
}
