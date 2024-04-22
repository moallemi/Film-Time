package io.filmtime.data.api.tmdb

import io.filmtime.data.model.CreditItem
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail

interface TmdbShowsRemoteSource {

  suspend fun showDetails(showId: Int): Result<VideoDetail, GeneralError>

  suspend fun trendingShows(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError>

  suspend fun popularShows(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError>

  suspend fun topRatedShows(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError>

  suspend fun onTheAirShows(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError>

  suspend fun airingTodayShows(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError>

  suspend fun getCredit(movieId: Int): Result<List<CreditItem>, GeneralError>
  suspend fun getSimilar(movieId: Int): Result<List<VideoThumbnail>, GeneralError>

  companion object {
    const val PAGE_SIZE = 20 // TMDB API default page size
  }
}
