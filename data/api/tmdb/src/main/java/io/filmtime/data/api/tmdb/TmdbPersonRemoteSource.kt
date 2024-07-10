package io.filmtime.data.api.tmdb

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.PersonDetail
import io.filmtime.data.model.Result

interface TmdbPersonRemoteSource {

  suspend fun getPerson(id: Int): Result<PersonDetail, GeneralError>
}
