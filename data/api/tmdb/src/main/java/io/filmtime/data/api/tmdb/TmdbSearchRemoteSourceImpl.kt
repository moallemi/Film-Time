package io.filmtime.data.api.tmdb

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.SearchResult
import io.filmtime.data.network.MovieSearchResult
import io.filmtime.data.network.PersonSearchResult
import io.filmtime.data.network.TmdbSearchService
import io.filmtime.data.network.TvShowSearchResult
import io.filmtime.data.network.adapter.NetworkResponse
import javax.inject.Inject

class TmdbSearchRemoteSourceImpl @Inject constructor(
  private val tmdbSearchService: TmdbSearchService,
) : TmdbSearchRemoteSource {
  override suspend fun searchMovies(page: Int, query: String): Result<List<SearchResult.Video>, GeneralError> =
    when (val result = tmdbSearchService.searchMovies(page, query)) {
      is NetworkResponse.Success -> {
        val videoListResponse = result.body?.results ?: emptyList()
        Result.Success(
          videoListResponse.map {
            SearchResult.Video(item = it.toVideoThumbnail())
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

  override suspend fun searchTvShows(page: Int, query: String): Result<List<SearchResult.TvShow>, GeneralError> =
    when (val result = tmdbSearchService.searchTvShows(page, query)) {
      is NetworkResponse.Success -> {
        val videoListResponse = result.body?.results ?: emptyList()
        Result.Success(
          videoListResponse.map {
            SearchResult.TvShow(item = it.toVideoThumbnail())
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

  override suspend fun searchAll(page: Int, query: String): Result<List<SearchResult>, GeneralError> =
    when (val result = tmdbSearchService.searchMulti(page, query)) {
      is NetworkResponse.Success -> {
        val videoListResponse = result.body?.results ?: emptyList()
        val mappedData: List<SearchResult> = videoListResponse.map {
          when (it) {
            is MovieSearchResult -> SearchResult.Video(item = it.toVideoThumbnail())
            is PersonSearchResult -> it.toPerson()
            is TvShowSearchResult -> SearchResult.TvShow(item = it.toVideoThumbnail())
          }
        }
        Result.Success(mappedData)
      }

      is NetworkResponse.ApiError -> {
        val errorResponse = result.body
        Result.Failure(GeneralError.ApiError(errorResponse.statusMessage, errorResponse.statusCode))
      }

      is NetworkResponse.NetworkError -> Result.Failure(GeneralError.NetworkError)
      is NetworkResponse.UnknownError -> Result.Failure(GeneralError.UnknownError(result.error))
    }
}
