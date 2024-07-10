package io.filmtime.domain.tmdb.person

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.PersonDetail
import io.filmtime.data.model.Result

interface GetPersonByIDUseCase {
  suspend operator fun invoke(id: Int): Result<PersonDetail, GeneralError>
}
