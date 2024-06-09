package io.filmtime.domain.tmdb.movies.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Person
import io.filmtime.data.model.Result
import io.filmtime.domain.tmdb.movies.GetMovieCreditsUseCase
import io.fimltime.data.tmdb.movies.TmdbMovieRepository
import javax.inject.Inject

class GetMovieCreditsUseCaseImpl @Inject constructor(
  private val tmdbMovieRepository: TmdbMovieRepository,
) : GetMovieCreditsUseCase {

  override suspend fun invoke(
    movieId: Int,
  ): Result<List<Person>, GeneralError> =
    tmdbMovieRepository.credits(movieId)
}
