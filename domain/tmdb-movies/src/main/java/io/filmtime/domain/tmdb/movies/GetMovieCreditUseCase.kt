package io.filmtime.domain.tmdb.movies

import io.filmtime.data.model.CreditItem
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail

interface GetMovieCreditUseCase {
  suspend operator fun invoke(movieId: Int): Result<List<CreditItem>, GeneralError>
}
