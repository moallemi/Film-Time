package io.filmtime.data.api.tmdb

import io.filmtime.data.model.CreditItem
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.network.TmdbCreditsResponse
import io.filmtime.data.network.TmdbErrorResponse
import io.filmtime.data.network.TmdbShowListResponse
import io.filmtime.data.network.TmdbShowsService
import io.filmtime.data.network.adapter.NetworkResponse
import javax.inject.Inject

class TmdbShowsRemoteSourceImpl @Inject constructor(
  private val tmdbShowsService: TmdbShowsService,
) : TmdbShowsRemoteSource {

  override suspend fun showDetails(showId: Int): Result<VideoDetail, GeneralError> =
    when (val result = tmdbShowsService.showDetails(seriesId = showId)) {
      is NetworkResponse.Success -> {
        val videoDetailResponse = result.body
        if (videoDetailResponse == null) {
          Result.Failure(GeneralError.UnknownError(Throwable("Video detail response is null")))
        } else {
          Result.Success(videoDetailResponse.toVideoDetail())
        }
      }

      is NetworkResponse.ApiError -> {
        val errorResponse = result.body
        Result.Failure(GeneralError.ApiError(errorResponse.statusMessage, errorResponse.statusCode))
      }

      is NetworkResponse.NetworkError -> Result.Failure(GeneralError.NetworkError)
      is NetworkResponse.UnknownError -> Result.Failure(GeneralError.UnknownError(result.error))
    }

  override suspend fun trendingShows(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError> =
    getShowsList {
      tmdbShowsService.trendingShows(timeWindow = TimeWindow.WEEK.value, page = page)
    }

  override suspend fun popularShows(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError> =
    getShowsList {
      tmdbShowsService.popular(page = page)
    }

  override suspend fun topRatedShows(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError> =
    getShowsList {
      tmdbShowsService.topRated(page = page)
    }

  override suspend fun onTheAirShows(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError> =
    getShowsList {
      tmdbShowsService.onTheAir(
        page = page,
        timezone = "America/New_York",
      )
    }

  override suspend fun airingTodayShows(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError> =
    getShowsList {
      tmdbShowsService.airingToday(
        page = page,
        timezone = "America/New_York",
      )
    }

  override suspend fun getCredit(seriesId: Int): Result<List<CreditItem>, GeneralError> =
    getCredits { tmdbShowsService.getCredit(seriesId) }

  override suspend fun getSimilar(seriesId: Int): Result<List<VideoThumbnail>, GeneralError> =
    getShowsList { tmdbShowsService.getSimilar(seriesId) }

  private suspend fun getCredits(
    apiFunction: suspend () -> NetworkResponse<TmdbCreditsResponse, TmdbErrorResponse>,
  ): Result<List<CreditItem>, GeneralError> =
    when (val result = apiFunction()) {
      is NetworkResponse.Success -> {
        val creditResponse = result.body?.cast ?: emptyList()
        Result.Success(creditResponse.map { it.toCreditItem() })
      }

      is NetworkResponse.ApiError -> {
        val errorResponse = result.body
        Result.Failure(GeneralError.ApiError(errorResponse.statusMessage, errorResponse.statusCode))
      }

      is NetworkResponse.NetworkError -> Result.Failure(GeneralError.NetworkError)
      is NetworkResponse.UnknownError -> Result.Failure(GeneralError.UnknownError(result.error))
    }

  private suspend fun getShowsList(
    apiCall: suspend () -> NetworkResponse<TmdbShowListResponse, TmdbErrorResponse>,
  ): Result<List<VideoThumbnail>, GeneralError> =
    when (val result = apiCall()) {
      is NetworkResponse.Success -> {
        val videoListResponse = result.body?.results ?: emptyList()
        Result.Success(videoListResponse.map { it.toVideoThumbnail() })
      }

      is NetworkResponse.ApiError -> {
        val errorResponse = result.body
        Result.Failure(GeneralError.ApiError(errorResponse.statusMessage, errorResponse.statusCode))
      }

      is NetworkResponse.NetworkError -> Result.Failure(GeneralError.NetworkError)
      is NetworkResponse.UnknownError -> Result.Failure(GeneralError.UnknownError(result.error))
    }

  private enum class TimeWindow(val value: String) {
    DAY("day"),
    WEEK("week"),
  }
}
