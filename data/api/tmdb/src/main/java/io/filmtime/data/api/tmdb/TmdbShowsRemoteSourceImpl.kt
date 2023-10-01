package io.filmtime.data.api.tmdb

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.network.TmdbErrorResponse
import io.filmtime.data.network.TmdbShowsService
import io.filmtime.data.network.TmdbVideoListResponse
import io.filmtime.data.network.adapter.NetworkResponse
import javax.inject.Inject

class TmdbShowsRemoteSourceImpl @Inject constructor(
  private val tmdbShowsService: TmdbShowsService,
) : TmdbShowsRemoteSource {

  override suspend fun getTrendingShows(): Result<List<VideoThumbnail>, GeneralError> =
    getShowsList {
      tmdbShowsService.getTrendingShows(timeWindow = TimeWindow.DAY.value)
    }

  private suspend fun getShowsList(
    apiCall: suspend () -> NetworkResponse<TmdbVideoListResponse, TmdbErrorResponse>,
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
