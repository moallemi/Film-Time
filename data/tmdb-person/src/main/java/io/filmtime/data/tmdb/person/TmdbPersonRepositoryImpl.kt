package io.filmtime.data.tmdb.person

import io.filmtime.data.api.tmdb.TmdbPersonRemoteSource
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.PersonDetail
import io.filmtime.data.model.Result
import javax.inject.Inject

internal class TmdbPersonRepositoryImpl @Inject constructor(
  private val tmdbPersonRemoteSource: TmdbPersonRemoteSource,
) : TmdbPersonRepository {

  override suspend fun getPerson(id: Int): Result<PersonDetail, GeneralError> =
    tmdbPersonRemoteSource.getPerson(id)
}
