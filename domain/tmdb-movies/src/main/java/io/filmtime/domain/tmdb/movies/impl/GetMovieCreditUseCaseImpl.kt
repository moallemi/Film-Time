package io.filmtime.domain.tmdb.movies.impl

import io.filmtime.data.model.CreditItem
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.domain.tmdb.movies.GetMovieCreditUseCase
import io.fimltime.data.tmdb.movies.TmdbMovieRepository
import javax.inject.Inject

class GetMovieCreditUseCaseImpl @Inject constructor(
  private val tmdbMovieRepository: TmdbMovieRepository,
) : GetMovieCreditUseCase {
  override suspend fun invoke(
    movieId: Int,
  ): Result<List<CreditItem>, GeneralError> = tmdbMovieRepository.getCredit(movieId)
}
