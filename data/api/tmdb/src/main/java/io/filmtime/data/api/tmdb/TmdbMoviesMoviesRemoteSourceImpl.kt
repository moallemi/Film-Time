package io.filmtime.data.api.tmdb

import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.network.TmdbMoviesService
import javax.inject.Inject

internal class TmdbMoviesMoviesRemoteSourceImpl @Inject constructor(
  private val tmdbMoviesService: TmdbMoviesService,
) : TmdbMoviesRemoteSource {

  override suspend fun getTrendingMovies(): List<VideoThumbnail> =
    tmdbMoviesService.getTrendingMovies()
      .results
      ?.map {
        it.toVideoThumbnail()
      } ?: emptyList()

  override suspend fun getMovieDetails(movieId: Int): VideoDetail =
    tmdbMoviesService.getMovieDetails(movieId)
      .toVideoDetail()
}
