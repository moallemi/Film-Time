package io.filmtime.domain.tmdb.shows

import io.filmtime.data.model.CreditItem
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.tmdb.shows.TmdbShowsRepository
import javax.inject.Inject

class GetShowCreditUseCaseImpl @Inject constructor(
  private val tmdbMovieRepository: TmdbShowsRepository,
) : GetShowCreditUseCase {
  override suspend fun invoke(
    movieId: Int,
  ): Result<List<CreditItem>, GeneralError> = tmdbMovieRepository.getCredit(movieId)
}
