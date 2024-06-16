package io.filmtime.data.api.trakt

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.network.adapter.NetworkResponse
import io.filmtime.data.network.trakt.TraktSearchService
import javax.inject.Inject

class TraktSearchRemoteSourceImpl @Inject constructor(
  private val traktIDLookupService: TraktSearchService,
) : TraktSearchRemoteSource {

  override suspend fun getByTmdbId(id: Int, type: TmdbType?): Result<Int, GeneralError> {
    return when (val result = traktIDLookupService.movieIDLookup(idType = "tmdb", id = id)) {
      is NetworkResponse.ApiError -> {
        val errorResponse = result.body
        Result.Failure(GeneralError.ApiError(errorResponse.error, result.code))
      }
      is NetworkResponse.NetworkError -> Result.Failure(GeneralError.NetworkError)
      is NetworkResponse.Success -> {
        val body = result.body ?: emptyList()
        val movieItemId = body.find { it.movie.ids.tmdb == id }?.movie?.ids?.trakt ?: -1
        return Result.Success(movieItemId)
      }

      is NetworkResponse.UnknownError -> Result.Failure(GeneralError.UnknownError(result.error))
    }
  }
}
