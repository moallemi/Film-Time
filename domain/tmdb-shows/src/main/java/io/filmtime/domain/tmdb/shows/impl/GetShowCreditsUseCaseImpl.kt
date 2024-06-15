package io.filmtime.domain.tmdb.shows.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Person
import io.filmtime.data.model.Result
import io.filmtime.data.tmdb.shows.TmdbShowsRepository
import io.filmtime.domain.tmdb.shows.GetShowCreditsUseCase
import javax.inject.Inject

internal class GetShowCreditsUseCaseImpl @Inject constructor(
  private val tmdbMovieRepository: TmdbShowsRepository,
) : GetShowCreditsUseCase {

  override suspend fun invoke(
    movieId: Int,
  ): Result<List<Person>, GeneralError> =
    tmdbMovieRepository.credits(movieId)
}
