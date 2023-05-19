package io.filmtime.data.api.tmdb

import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.network.TmdbMoviesService
import javax.inject.Inject

internal class TmdbMoviesMoviesRemoteSourceImpl @Inject constructor(
  private val tmdbMoviesService: TmdbMoviesService,
) : TmdbMoviesRemoteSource {

  override suspend fun getTrendingMovies(): List<VideoThumbnail> =
    getMovieList(ListType.DAY)

  override suspend fun getPopularMovies(): List<VideoThumbnail> =
    getMovieList(ListType.POPULAR)

  override suspend fun getTopRatedMovies(): List<VideoThumbnail> =
    getMovieList(ListType.TOP_RATED)

  override suspend fun getNowPlayingMovies(): List<VideoThumbnail> =
    getMovieList(ListType.NOW_PLAYING)

  override suspend fun getMovieDetails(movieId: Int): VideoDetail =
    tmdbMoviesService.getMovieDetails(movieId)
      .toVideoDetail()

  private suspend fun getMovieList(type: ListType) =
    tmdbMoviesService.getMovieList(type.value)
      .results
      ?.map {
        it.toVideoThumbnail()
      } ?: emptyList()

  private enum class ListType(val value: String) {
    DAY("day"),
    WEEK("week"),
    POPULAR("popular"),
    TOP_RATED("top_rated"),
    NOW_PLAYING("now_playing"),
  }
}
