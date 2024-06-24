package io.filmtime.data.api.trakt

import io.filmtime.data.api.trakt.model.toRatings
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Ratings
import io.filmtime.data.model.Result
import io.filmtime.data.network.adapter.NetworkResponse.ApiError
import io.filmtime.data.network.adapter.NetworkResponse.NetworkError
import io.filmtime.data.network.adapter.NetworkResponse.Success
import io.filmtime.data.network.adapter.NetworkResponse.UnknownError
import io.filmtime.data.network.trakt.TraktService
import javax.inject.Inject

internal class TraktRemoteSourceImpl @Inject constructor(
  private val traktService: TraktService,
) : TraktRemoteSource {

  override suspend fun ratings(type: String, traktId: Int): Result<Ratings, GeneralError> =
    when (val result = traktService.ratings(type, traktId)) {
      is Success -> Result.Success(result.body?.toRatings() ?: Ratings())
      is ApiError -> Result.Failure(GeneralError.ApiError(result.body.error, result.code))
      is NetworkError -> Result.Failure(GeneralError.NetworkError)
      is UnknownError -> Result.Failure(GeneralError.UnknownError(result.error))
    }
}
