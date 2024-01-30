package io.filmtime.data.api.tmdb

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.network.TmdbErrorResponse
import io.filmtime.data.network.TmdbMoviesService
import io.filmtime.data.network.TmdbVideoListResponse
import io.filmtime.data.network.adapter.NetworkResponse
import javax.inject.Inject

internal class TmdbMoviesRemoteSourceImpl @Inject constructor(
  private val tmdbMoviesService: TmdbMoviesService,
) : TmdbMoviesRemoteSource {

  override suspend fun getTrendingMovies(): Result<List<VideoThumbnail>, GeneralError> =
    getMovieList {
      tmdbMoviesService.trending(ListType.DAY.value)
    }

  override suspend fun getPopularMovies(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError> =
    getMovieList {
      tmdbMoviesService.popular(
        page = page,
      )
    }

  override suspend fun getTopRatedMovies(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError> =
    getMovieList {
      tmdbMoviesService.topRated(
        page = page,
      )
    }

  override suspend fun getNowPlayingMovies(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError> =
    getMovieList {
      tmdbMoviesService.nowPlaying(
        page = page,
      )
    }

  override suspend fun getMovieDetails(movieId: Int): Result<VideoDetail, GeneralError> =
    when (val result = tmdbMoviesService.getMovieDetails(movieId = movieId)) {
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

  private suspend fun getMovieList(
    apiFunction: suspend () -> NetworkResponse<TmdbVideoListResponse, TmdbErrorResponse>,
  ): Result<List<VideoThumbnail>, GeneralError> =
    when (val result = apiFunction()) {
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

  private enum class ListType(val value: String) {
    DAY("day"),
    WEEK("week"),
  }
}
