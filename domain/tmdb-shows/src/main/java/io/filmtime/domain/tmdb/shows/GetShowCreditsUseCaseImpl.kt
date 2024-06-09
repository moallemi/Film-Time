package io.filmtime.domain.tmdb.shows

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Person
import io.filmtime.data.model.Result
import io.filmtime.data.tmdb.shows.TmdbShowsRepository
import javax.inject.Inject

class GetShowCreditsUseCaseImpl @Inject constructor(
  private val tmdbMovieRepository: TmdbShowsRepository,
) : GetShowCreditsUseCase {

  override suspend fun invoke(
    movieId: Int,
  ): Result<List<Person>, GeneralError> =
    tmdbMovieRepository.credits(movieId)
}
