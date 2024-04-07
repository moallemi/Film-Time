package io.filmtime.data.api.tmdb

import android.util.Log
import io.filmtime.data.model.CreditItem
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.network.TmdbCreditsResponse
import io.filmtime.data.network.TmdbErrorResponse
import io.filmtime.data.network.TmdbMoviesService
import io.filmtime.data.network.TmdbVideoListResponse
import io.filmtime.data.network.adapter.NetworkResponse
import javax.inject.Inject

internal class TmdbMoviesRemoteSourceImpl @Inject constructor(
  private val tmdbMoviesService: TmdbMoviesService,
) : TmdbMoviesRemoteSource {

  override suspend fun trendingMovies(page: Int): Result<List<VideoThumbnail>, GeneralError> =
    getMovieList {
      tmdbMoviesService.trending(
        timeWindow = ListType.WEEK.value,
        page = page,
      )
    }

  override suspend fun popularMovies(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError> =
    getMovieList {
      tmdbMoviesService.popular(
        page = page,
      )
    }

  override suspend fun topRatedMovies(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError> =
    getMovieList {
      tmdbMoviesService.topRated(
        page = page,
      )
    }

  override suspend fun nowPlayingMovies(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError> =
    getMovieList {
      tmdbMoviesService.nowPlaying(
        page = page,
      )
    }

  override suspend fun getCredit(movieId: Int): Result<List<CreditItem>, GeneralError> =
    getCredits { tmdbMoviesService.getCredit(movieId) }

  override suspend fun getSimilar(movieId: Int): Result<List<VideoThumbnail>, GeneralError> =
    getMovieList {
      tmdbMoviesService.getSimilar(
        movieId = movieId,
      )
    }

  override suspend fun upcomingMovies(
    page: Int,
  ): Result<List<VideoThumbnail>, GeneralError> =
    getMovieList {
      tmdbMoviesService.upcoming(
        page = page,
      )
    }

  override suspend fun movieDetails(movieId: Int): Result<VideoDetail, GeneralError> =
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
        Result.Success(
          videoListResponse.map {
            Log.d("date", "${ it.releaseDate } | ${it.title}")
            it.toVideoThumbnail()
          },
        )
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
}
