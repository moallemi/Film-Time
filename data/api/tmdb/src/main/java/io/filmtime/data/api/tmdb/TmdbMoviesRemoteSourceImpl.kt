package io.filmtime.data.api.tmdb

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.MovieCollection
import io.filmtime.data.model.Person
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.network.TmdbCollectionService
import io.filmtime.data.network.TmdbErrorResponse
import io.filmtime.data.network.TmdbMoviesService
import io.filmtime.data.network.TmdbVideoListResponse
import io.filmtime.data.network.adapter.NetworkResponse
import javax.inject.Inject

internal class TmdbMoviesRemoteSourceImpl @Inject constructor(
  private val tmdbMoviesService: TmdbMoviesService,
  private val tmdbCollectionService: TmdbCollectionService,
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

  override suspend fun credits(movieId: Int): Result<List<Person>, GeneralError> =
    when (val result = tmdbMoviesService.credits(movieId)) {
      is NetworkResponse.Success -> {
        val castsResponse = result.body?.cast.orEmpty()
        val crewsResponse = result.body?.crew.orEmpty()

        Result.Success(
          data = castsResponse.map { it.asCastItem() } + crewsResponse.map { it.asCrewItem() },
        )
      }

      is NetworkResponse.ApiError -> {
        val errorResponse = result.body
        Result.Failure(GeneralError.ApiError(errorResponse.statusMessage, errorResponse.statusCode))
      }

      is NetworkResponse.NetworkError -> Result.Failure(GeneralError.NetworkError)
      is NetworkResponse.UnknownError -> Result.Failure(GeneralError.UnknownError(result.error))
    }

  override suspend fun getSimilar(movieId: Int): Result<List<VideoThumbnail>, GeneralError> =
    getMovieList {
      tmdbMoviesService.getSimilar(
        movieId = movieId,
      )
    }

  override suspend fun getCollection(collectionId: Int): Result<MovieCollection, GeneralError> =
    when (val result = tmdbCollectionService.getCollection(collectionId)) {
      is NetworkResponse.Success -> {
        val collectionResponse = result.body
        if (collectionResponse == null) {
          Result.Failure(GeneralError.UnknownError(Throwable("Video collection response is null")))
        } else {
          Result.Success(collectionResponse.toCollection())
        }
      }
      is NetworkResponse.ApiError -> {
        val errorResponse = result.body
        Result.Failure(GeneralError.ApiError(errorResponse.statusMessage, errorResponse.statusCode))
      }

      is NetworkResponse.NetworkError -> Result.Failure(GeneralError.NetworkError)
      is NetworkResponse.UnknownError -> Result.Failure(GeneralError.UnknownError(result.error))
    }

  override suspend fun searchMovie(query: String): Result<List<VideoThumbnail>, GeneralError> =
    getMovieList {
      tmdbMoviesService.searchMovies(query)
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
}
