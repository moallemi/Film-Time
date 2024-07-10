package io.filmtime.domain.tmdb.person.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.PersonDetail
import io.filmtime.data.model.Result
import io.filmtime.data.tmdb.person.TmdbPersonRepository
import io.filmtime.domain.tmdb.person.GetPersonByIDUseCase
import javax.inject.Inject

internal class GetPersonByIDUseCaseImpl @Inject constructor(
  private val repository: TmdbPersonRepository,
) : GetPersonByIDUseCase {
  override suspend fun invoke(id: Int): Result<PersonDetail, GeneralError> =
    repository.getPerson(id)
}
