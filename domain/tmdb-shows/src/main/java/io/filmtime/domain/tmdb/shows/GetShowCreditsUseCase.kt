package io.filmtime.domain.tmdb.shows

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Person
import io.filmtime.data.model.Result

interface GetShowCreditsUseCase {
  suspend operator fun invoke(movieId: Int): Result<List<Person>, GeneralError>
}
