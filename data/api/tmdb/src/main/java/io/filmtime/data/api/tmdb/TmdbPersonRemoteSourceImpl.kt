package io.filmtime.data.api.tmdb

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.PersonDetail
import io.filmtime.data.model.Result
import io.filmtime.data.network.adapter.NetworkResponse.ApiError
import io.filmtime.data.network.adapter.NetworkResponse.NetworkError
import io.filmtime.data.network.adapter.NetworkResponse.Success
import io.filmtime.data.network.adapter.NetworkResponse.UnknownError
import io.filmtime.data.network.trakt.TraktPersonService
import javax.inject.Inject

internal class TmdbPersonRemoteSourceImpl @Inject constructor(
  private val personService: TraktPersonService,
) : TmdbPersonRemoteSource {

  override suspend fun getPerson(id: Int): Result<PersonDetail, GeneralError> =
    when (val result = personService.getPerson(id.toString())) {
      is Success -> Result.Success(result.body!!.toPerson())
      is ApiError -> Result.Failure(GeneralError.ApiError(result.body.error, result.code))
      is NetworkError -> Result.Failure(GeneralError.NetworkError)
      is UnknownError -> Result.Failure(GeneralError.UnknownError(result.error))
    }
}
