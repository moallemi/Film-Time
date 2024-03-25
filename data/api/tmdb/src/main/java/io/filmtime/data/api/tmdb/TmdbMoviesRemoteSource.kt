package io.filmtime.data.api.tmdb

import io.filmtime.data.model.CreditItem
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail

interface TmdbMoviesRemoteSource {

  suspend fun movieDetails(movieId: Int): Result<VideoDetail, GeneralError>

  suspend fun trendingMovies(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError>

  suspend fun popularMovies(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError>

  suspend fun topRatedMovies(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError>

  suspend fun nowPlayingMovies(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError>

  suspend fun upcomingMovies(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError>
  suspend fun getCredit(movieId: Int): Result<List<CreditItem>, GeneralError>
  suspend fun getSimilar(movieId: Int): Result<List<VideoThumbnail>, GeneralError>
  companion object {
    const val PAGE_SIZE = 20 // TMDB API default page size
  }
}
