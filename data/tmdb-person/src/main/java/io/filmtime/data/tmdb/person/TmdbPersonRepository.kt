package io.filmtime.data.tmdb.person

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.PersonDetail
import io.filmtime.data.model.Result

interface TmdbPersonRepository {
  suspend fun getPerson(id: Int): Result<PersonDetail, GeneralError>
}
