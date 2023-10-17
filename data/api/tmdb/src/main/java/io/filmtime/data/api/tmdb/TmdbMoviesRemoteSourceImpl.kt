package io.filmtime.data.api.tmdb

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.network.TmdbMoviesService
import io.filmtime.data.network.adapter.NetworkResponse
import javax.inject.Inject

internal class TmdbMoviesRemoteSourceImpl @Inject constructor(
  private val tmdbMoviesService: TmdbMoviesService,
) : TmdbMoviesRemoteSource {

  override suspend fun getTrendingMovies(page:Long): Result<List<VideoThumbnail>, GeneralError> =
    getMovieList(ListType.DAY,page)

  override suspend fun getPopularMovies(page:Long): Result<List<VideoThumbnail>, GeneralError> =
    getMovieList(ListType.POPULAR, page)

  override suspend fun getTopRatedMovies(page: Long): Result<List<VideoThumbnail>, GeneralError> =
    getMovieList(ListType.TOP_RATED, page)

  override suspend fun getNowPlayingMovies(page: Long): Result<List<VideoThumbnail>, GeneralError> =
    getMovieList(ListType.NOW_PLAYING,page)

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

  private suspend fun getMovieList(type: ListType, page: Long): Result<List<VideoThumbnail>, GeneralError> =
    when (val result = tmdbMoviesService.getMovieList(type.value, page=page)) {
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
    POPULAR("popular"),
    TOP_RATED("top_rated"),
    NOW_PLAYING("now_playing"),
  }
}
