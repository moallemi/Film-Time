package io.filmtime.domain.tmdb.movies

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Person
import io.filmtime.data.model.Result

interface GetMovieCreditsUseCase {
  suspend operator fun invoke(movieId: Int): Result<List<Person>, GeneralError>
}
